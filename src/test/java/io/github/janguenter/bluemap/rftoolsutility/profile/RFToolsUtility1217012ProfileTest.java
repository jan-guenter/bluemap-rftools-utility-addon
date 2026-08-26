/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsutility.profile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RFToolsUtility1217012ProfileTest {

    @Test
    void ownsOnlyTheExactTankHost() {
        assertTrue(RFToolsUtility1217012Profile.owns("rftoolsutility:tank"));
        assertFalse(RFToolsUtility1217012Profile.owns("rftoolsutility:screen"));
        assertFalse(RFToolsUtility1217012Profile.owns("rftoolsbuilder:tank"));
    }
}
