#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Family-owned cases for the bounded RFTools Utility gallery."""

from __future__ import annotations

from dataclasses import dataclass


NAMESPACE = "rftoolsutility_gallery"
ENVELOPE = (160, 99, 160, 188, 103, 179)
AUDIT_ORIGIN = (162, 100, 163)
AUDIT_COLUMNS = 9
AUDIT_X_STEP = 3
AUDIT_Z_STEP = 4


@dataclass(frozen=True)
class Placement:
    case_id: str
    label: str
    x: int
    y: int
    z: int
    block_state: str
    expected: str


DEFAULT_BLOCKS = (
    ("analog", "Analog"),
    ("counter", "Counter"),
    ("crafter1", "Crafter Tier 1"),
    ("crafter2", "Crafter Tier 2"),
    ("crafter3", "Crafter Tier 3"),
    ("creative_screen", "Creative Screen"),
    ("destination_analyzer", "Destination Analyzer"),
    ("dialing_device", "Dialing Device"),
    ("digit", "Digit"),
    ("environmental_controller", "Environmental Controller"),
    ("invchecker", "Inventory Checker"),
    ("logic", "Logic"),
    ("matter_beamer", "Matter Beamer"),
    ("matter_booster", "Matter Booster"),
    ("matter_receiver", "Matter Receiver"),
    ("matter_transmitter", "Matter Transmitter"),
    ("redstone_receiver", "Redstone Receiver"),
    ("redstone_transmitter", "Redstone Transmitter"),
    ("screen", "Screen"),
    ("screen_controller", "Screen Controller"),
    ("sensor", "Sensor"),
    ("sequencer", "Sequencer"),
    ("simple_dialer", "Simple Dialer"),
    ("spawner", "Spawner"),
    ("tank", "Tank"),
    ("timer", "Timer"),
    ("wire", "Wire"),
)


def audit_placements() -> tuple[Placement, ...]:
    origin_x, origin_y, origin_z = AUDIT_ORIGIN
    return tuple(
        Placement(
            f"default-{block_name.replace('_', '-')}",
            f"{label} default block",
            origin_x + (index % AUDIT_COLUMNS) * AUDIT_X_STEP,
            origin_y,
            origin_z + (index // AUDIT_COLUMNS) * AUDIT_Z_STEP,
            f"rftoolsutility:{block_name}",
            "default-visible",
        )
        for index, (block_name, label) in enumerate(DEFAULT_BLOCKS)
    )


TANK_ARRANGEMENT = (
    Placement(
        "tank-cluster-base",
        "Tank cluster base",
        162,
        100,
        176,
        "rftoolsutility:tank",
        "adjacent-visible",
    ),
    Placement(
        "tank-cluster-east",
        "Tank cluster east neighbor",
        163,
        100,
        176,
        "rftoolsutility:tank",
        "adjacent-visible",
    ),
    Placement(
        "tank-cluster-above",
        "Tank cluster upper neighbor",
        162,
        101,
        176,
        "rftoolsutility:tank",
        "adjacent-visible",
    ),
)


STONE_CONTROL = Placement(
    "stock-control",
    "Stone stock rendering control",
    168,
    100,
    176,
    "minecraft:stone",
    "stock-visible",
)


PLACEMENTS = audit_placements() + TANK_ARRANGEMENT + (STONE_CONTROL,)
