# BlueMap RFTools Utility Add-on

A Java 21 BlueMap add-on for the exact `rftoolsutility-1.21-7.0.12` profile in All the Mons
`1.2.0` / Minecraft `1.21.1`.

Status: visual-review prototype. The exact artifact gate and BlueMap 5.22
adapter replace the unsupported custom tank loader with the client's stable
empty-tank cube and installed textures. The other RFTools Utility blocks retain
stock rendering for the first comparison pass.

## Build

```bash
gradle --no-daemon -PbluemapSourcePath=../bluemap-backport clean check build
```

`check` is the quick Java/checkstyle/archive gate. `prototypeCheck` additionally
requires every exact candidate JAR property and validates the comparison
gallery. See `provenance/upstreams.json` for immutable artifact identities and
the [execution guide](docs/EXECUTION.md) for the prototype-to-release loop.

## Install

Place the production JAR in BlueMap's add-on pack directory and restart the
BlueMap JVM. Removal plus one restart restores stock behavior; the add-on
creates no custom world state.

Set `-Dbluemap.rftoolsutility.disabled=true` to leave the exact profile inactive.

## Scope boundary

The first profile owns only `rftoolsutility:tank` and deliberately renders its
empty `tank0` exterior. Live fluid fill, screen text, digits, beams, activity
overlays, particles, animation phase, and unsupported states stay stock unless
the comparison proves another material static defect.

No RFTools Utility binary, source, class, asset, captured mesh, or gallery is
bundled in the add-on.
