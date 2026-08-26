#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Lint the generated RFTools Utility gallery without starting Minecraft."""

from __future__ import annotations

from collections import Counter
import json
from pathlib import Path
import re
import sys

sys.dont_write_bytecode = True
import cases
import generate


ROOT = Path(__file__).resolve().parent


def fail(message: str) -> None:
    raise ValueError(message)


def main() -> int:
    for relative, payload in generate.generated_files().items():
        path = ROOT / relative
        if not path.is_file() or path.read_bytes() != payload:
            fail(f"generated file differs: {relative}")

    json.loads((ROOT / "datapack/pack.mcmeta").read_text(encoding="utf-8"))
    load_tag = json.loads(
        (ROOT / "datapack/data/minecraft/tags/function/load.json").read_text(
            encoding="utf-8"
        )
    )
    if load_tag != {"values": [f"{cases.NAMESPACE}:load"]}:
        fail("load tag differs from the exact namespace")

    if len(cases.DEFAULT_BLOCKS) != 27:
        fail("default audit must contain exactly 27 RFTools Utility blocks")
    if len(cases.TANK_ARRANGEMENT) != 3:
        fail("tank arrangement must contain exactly three extra tanks")
    if len(cases.PLACEMENTS) != 31:
        fail("gallery must contain 27 defaults, three extra tanks, and stone")

    expected_default_ids = {
        f"rftoolsutility:{block_name}"
        for block_name, _label in cases.DEFAULT_BLOCKS
    }
    audit = cases.PLACEMENTS[: len(cases.DEFAULT_BLOCKS)]
    if {placement.block_state for placement in audit} != expected_default_ids:
        fail("default audit block census differs from the exact 27-block list")
    if any(placement.expected != "default-visible" for placement in audit):
        fail("every default audit row must expect visible stock geometry")

    block_ids = Counter(placement.block_state for placement in cases.PLACEMENTS)
    expected_ids = Counter(expected_default_ids)
    expected_ids["rftoolsutility:tank"] += 3
    expected_ids["minecraft:stone"] = 1
    if block_ids != expected_ids:
        fail(f"unexpected gallery block census: {block_ids}")

    expected_tanks = {
        (162, 100, 176),
        (163, 100, 176),
        (162, 101, 176),
    }
    actual_tanks = {
        (placement.x, placement.y, placement.z)
        for placement in cases.TANK_ARRANGEMENT
    }
    if actual_tanks != expected_tanks:
        fail("tank arrangement must retain one east neighbor and one upper neighbor")
    if any(
        placement.block_state != "rftoolsutility:tank"
        or placement.expected != "adjacent-visible"
        for placement in cases.TANK_ARRANGEMENT
    ):
        fail("tank arrangement must use three default empty tank blocks")

    stock_controls = [
        placement
        for placement in cases.PLACEMENTS
        if placement.block_state == "minecraft:stone"
    ]
    if stock_controls != [cases.STONE_CONTROL]:
        fail("gallery must contain exactly the declared stone stock control")

    coordinates = [
        (placement.x, placement.y, placement.z)
        for placement in cases.PLACEMENTS
    ]
    if len(coordinates) != len(set(coordinates)):
        fail("gallery coordinates must be unique")
    minimum_x, minimum_y, minimum_z, maximum_x, maximum_y, maximum_z = (
        cases.ENVELOPE
    )
    for coordinate in coordinates:
        x, y, z = coordinate
        if not (
            minimum_x <= x <= maximum_x
            and minimum_y <= y <= maximum_y
            and minimum_z <= z <= maximum_z
        ):
            fail(f"placement escaped its bounded envelope: {coordinate}")

    if any(
        "[" in placement.block_state or "{" in placement.block_state
        for placement in cases.PLACEMENTS
    ):
        fail("gallery rows must use bare default block IDs without state or NBT")
    if any(
        "screen_hitblock" in placement.block_state
        for placement in cases.PLACEMENTS
    ):
        fail("the internal screen hit-block helper is outside the default audit")

    function_root = ROOT / f"datapack/data/{cases.NAMESPACE}/function"
    build = (function_root / "build.mcfunction").read_text(encoding="utf-8")
    clear = (function_root / "clear.mcfunction").read_text(encoding="utf-8")
    verify = (function_root / "verify.mcfunction").read_text(encoding="utf-8")
    functions = "\n".join(
        path.read_text(encoding="utf-8")
        for path in sorted(function_root.glob("*.mcfunction"))
    )
    if len(re.findall(r"^setblock ", build, re.MULTILINE)) != 31:
        fail("build must place exactly 31 audited blocks")
    if len(re.findall(r"^execute unless block ", verify, re.MULTILINE)) != 31:
        fail("verify must check exactly 31 audited blocks")
    expected_clear = "fill 160 99 160 188 103 179 minecraft:air"
    clear_commands = re.findall(r"^fill .* minecraft:air$", clear, re.MULTILINE)
    if clear_commands != [expected_clear]:
        fail("clear must cover exactly the bounded gallery envelope")

    lowered = functions.lower()
    for forbidden in (
        "summon ",
        "data merge",
        "data modify",
        "data remove",
        " op ",
        "deop ",
        "stop ",
    ):
        if forbidden in lowered:
            fail(f"forbidden gallery command: {forbidden}")

    print(
        "RFTools Utility gallery lint passed: 27 default blocks, "
        "3 adjacent/stacked tanks, 1 stone control"
    )
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, ValueError) as error:
        print(f"gallery lint failed: {error}", file=sys.stderr)
        raise SystemExit(1)
