/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsutility.profile;

import java.util.List;
import java.util.Set;

/** Exact All the Mons 1.2.0 profile `rftoolsutility-1.21-7.0.12`. */
public final class RFToolsUtility1217012Profile {

    public static final String PROFILE_ID = "rftoolsutility-1.21-7.0.12";
    public static final String TANK_ID = "rftoolsutility:tank";
    public static final Set<String> OWNED_BLOCKS = Set.of(TANK_ID);
    public static final List<ArtifactPin> ARTIFACTS = List.of(
            new ArtifactPin(
                    "rftoolsUtility",
                    "rftoolsutility",
                    "1.21-7.0.12",
                    "rftoolsutility-1.21-7.0.12.jar",
                    1_434_987L,
                    "1fdbf7505c6d6f4ef93b8b15961c9c1a6a4d35a5d676297f8f647916238f4d2a"
            ),
            new ArtifactPin(
                    "mcjtylib",
                    "mcjtylib",
                    "1.21-9.0.21",
                    "mcjtylib-1.21-9.0.21.jar",
                    699_982L,
                    "b8eca900d4fe77a495c74137bd6be79c67281e5d7ae67ca55f59980c64960a0e"
            ),
            new ArtifactPin(
                    "rftoolsBase",
                    "rftoolsbase",
                    "1.21-6.0.11",
                    "rftoolsbase-1.21-6.0.11.jar",
                    463_973L,
                    "5195ba530e6cf9ba61c9954a3297679e6d29aa1b6182e27ae14ea43463dd4b00"
            )
    );

    private RFToolsUtility1217012Profile() {
    }

    public static boolean owns(String blockId) {
        return OWNED_BLOCKS.contains(blockId);
    }
}
