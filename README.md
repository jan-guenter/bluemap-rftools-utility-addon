# BlueMap RFTools Utility Add-on

A Java 21 BlueMap add-on for the exact `rftoolsutility-1.21-7.0.12` profile in All the Mons
`1.2.0` / Minecraft `1.21.1`.

Version `0.1.0-alpha.2` is the owner-accepted native BlueMap 5.23 release
candidate. It preserves the owner-accepted `0.1.0-alpha.1` contract. The exact
artifact gate replaces the unsupported custom tank loader with the client's
stable empty-tank cube and installed textures. The other RFTools Utility blocks
retain stock rendering for the first comparison pass.

## Build

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon -PbluemapSourcePath=/path/to/BlueMap-at-7e07f4e7 \
  -PrftoolsUtilityJar=/path/to/rftoolsutility-1.21-7.0.12.jar \
  -PmcjtylibJar=/path/to/mcjtylib-1.21-9.0.21.jar \
  -PrftoolsBaseJar=/path/to/rftoolsbase-1.21-6.0.11.jar \
  -PreleaseTag=v0.1.0-alpha.2 clean prototypeCheck build \
  generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyReleaseCandidate
```

`check` is the quick Java/checkstyle/archive gate. `prototypeCheck` additionally
requires every exact candidate JAR property and validates the comparison
gallery. See `provenance/upstreams.json` for immutable artifact identities and
the [execution guide](docs/EXECUTION.md) for the prototype-to-release loop.

The add-on compiles the four helpers from the exact Adapter API source-module
gitlink. Its standalone JAR is neither installed nor nested.

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
