# RFTools Utility staging gallery

This directory defines the bounded deterministic datapack used to review the
exact RFTools Utility `1.21-7.0.12` BlueMap prototype. The operator-installed
runtime JAR is 1,434,987 bytes with SHA-256
`1fdbf7505c6d6f4ef93b8b15961c9c1a6a4d35a5d676297f8f647916238f4d2a`.

The fixture covers the 27 translated default block IDs present in that exact
artifact. It adds three empty tanks in an L-shaped adjacent-and-stacked case
and one `minecraft:stone` stock control. The internal `screen_hitblock` model
helper has no translated block entry and is outside this audit.

All commands place bare default block IDs. The gallery does not write
block-entity NBT, fluids, screen modules, teleport destinations, crafting
recipes, spawner matter, or runtime activity. Those states need separately
proven saved data before they belong in a fixture.

## Layout

| Section | Coordinates | Count | Purpose |
| --- | --- | ---: | --- |
| Default audit | x `162..186`, y `100`, z `163..171` | 27 | one default placement for each translated RFTools Utility block ID |
| Tank arrangement | `(162,100,176)`, `(163,100,176)`, `(162,101,176)` | 3 | horizontal adjacency and one vertical stack using empty default tanks |
| Stock control | `(168,100,176)` | 1 | ordinary Minecraft stone rendering |

The inclusive clear envelope is x `160..188`, y `99..103`, z `160..179`.
The 31 asserted placements contain no copied models, textures, source, or
captured meshes.

The deterministic gallery ZIP is 2,942 bytes with SHA-256
`63393b50de6684e925cec0ddbee6d3818dfa8cf7f8d90f8de9fdb46ca85c4767`.

## Generate, lint, and package

Run from the repository root:

```bash
PYTHONDONTWRITEBYTECODE=1 python3 gallery/generate.py
PYTHONDONTWRITEBYTECODE=1 python3 gallery/generate.py --check
PYTHONDONTWRITEBYTECODE=1 python3 gallery/lint.py
bash gallery/package.sh /tmp/rftoolsutility-gallery.zip
```

`generate.py` rewrites only the generated ledger, datapack files, and
`SHA256SUMS`. The package script checks those files and creates a deterministic
ZIP from sorted paths with fixed modes and timestamps.

## Staging functions

```text
/function rftoolsutility_gallery:build
/function rftoolsutility_gallery:verify
/function rftoolsutility_gallery:clear
/function rftoolsutility_gallery:release
```

`build` clears the bounded envelope, places all 31 blocks, and runs the exact
block-ID checks. `release` clears only this disposable fixture.
