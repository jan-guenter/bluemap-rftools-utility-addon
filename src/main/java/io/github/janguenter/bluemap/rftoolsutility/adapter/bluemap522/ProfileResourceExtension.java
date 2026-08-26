/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsutility.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockProperties;
import de.bluecolored.bluemap.core.world.BlockState;
import io.github.janguenter.bluemap.rftoolsutility.activation.AddonRuntime;
import io.github.janguenter.bluemap.rftoolsutility.profile.ExactArtifactDetector;
import io.github.janguenter.bluemap.rftoolsutility.profile.RFToolsUtility1217012Profile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/** Exact-artifact admission hook; family routing deliberately remains stock. */
final class ProfileResourceExtension implements ResourcePackExtension {

    static final Key SYNTHETIC_TANK = Key.parse("bluemap_rftoolsutility:tank");
    static final Key TANK_SIDE = Key.parse("rftoolsutility:block/tank0");
    static final Key MACHINE_TOP = Key.parse("rftoolsbase:block/base/machinetop");
    static final Key MACHINE_BOTTOM = Key.parse("rftoolsbase:block/base/machinebottom");
    static final Set<Key> REQUIRED_TEXTURES = Set.of(
            TANK_SIDE,
            MACHINE_TOP,
            MACHINE_BOTTOM
    );

    private final ResourcePack resourcePack;
    private final AddonRuntime runtime;

    ProfileResourceExtension(ResourcePack resourcePack, AddonRuntime runtime) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.rftoolsutility.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactArtifactDetector.matchesAll(roots, RFToolsUtility1217012Profile.ARTIFACTS)) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }

        if (!validDispatch(resourcePack.getBlockStates().get(SYNTHETIC_TANK))) {
            runtime.inactive("synthetic-dispatch-invalid");
            return;
        }
        runtime.activate();
    }

    @Override
    public Set<Key> collectUsedTextureKeys() {
        return runtime.active() ? REQUIRED_TEXTURES : Set.of();
    }

    @Override
    public void bake() {
        if (!runtime.active()) {
            return;
        }
        try {
            for (Key texture : REQUIRED_TEXTURES) {
                if (!validTexture(texture)) {
                    runtime.inactive("tank-texture-invalid");
                    return;
                }
            }
        } catch (IOException | RuntimeException exception) {
            runtime.inactive("tank-texture-unreadable");
            return;
        }
        System.out.println("BlueMap RFTools Utility add-on active: routed exact empty tank.");
    }

    @Override
    public Key getBlockStateKey(Key key) {
        return runtime.active() && RFToolsUtility1217012Profile.owns(key.getFormatted())
                ? SYNTHETIC_TANK : key;
    }

    @Override
    public void getBlockProperties(BlockState state, BlockProperties.Builder builder) {
        if (runtime.active()
                && RFToolsUtility1217012Profile.owns(state.getId().getFormatted())) {
            builder.culling(false).cullingIdentical(false);
        }
    }

    private boolean validTexture(Key key) throws IOException {
        Texture texture = resourcePack.getTextures().get(key);
        if (texture == null || texture.getAnimation() != null) {
            return false;
        }
        BufferedImage image = texture.getTextureImage();
        return image != null && image.getWidth() == 16 && image.getHeight() == 16;
    }

    private static boolean validDispatch(
            de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState state
    ) {
        if (state == null || state.getMultipart() != null) {
            return false;
        }
        Variants variants = state.getVariants();
        if (variants == null || variants.getDefaultVariant() == null) {
            return false;
        }
        VariantSet set = variants.getDefaultVariant();
        return set.getVariants().length == 1
                && BlueMap522Adapter.isExpectedDispatch(set.getVariants()[0]);
    }
}
