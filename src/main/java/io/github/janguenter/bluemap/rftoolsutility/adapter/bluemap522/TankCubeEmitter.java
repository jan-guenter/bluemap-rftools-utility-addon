/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.rftoolsutility.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.TileModel;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;

import java.util.Map;

/** Emits the six opaque faces of the client's empty-tank model. */
final class TankCubeEmitter {

    private final Map<Key, Integer> materials;

    TankCubeEmitter(TextureGallery textures) {
        this.materials = Map.of(
                ProfileResourceExtension.TANK_SIDE,
                require(textures, ProfileResourceExtension.TANK_SIDE),
                ProfileResourceExtension.MACHINE_TOP,
                require(textures, ProfileResourceExtension.MACHINE_TOP),
                ProfileResourceExtension.MACHINE_BOTTOM,
                require(textures, ProfileResourceExtension.MACHINE_BOTTOM)
        );
    }

    void emit(BlockNeighborhood block, TileModelView target) {
        for (Direction direction : Direction.values()) {
            Key texture = switch (direction) {
                case UP -> ProfileResourceExtension.MACHINE_TOP;
                case DOWN -> ProfileResourceExtension.MACHINE_BOTTOM;
                default -> ProfileResourceExtension.TANK_SIDE;
            };
            quad(block, target, direction, materials.get(texture));
        }
    }

    private static int require(TextureGallery textures, Key key) {
        int material = textures.get(key);
        if (material <= 0) {
            throw new IllegalStateException("tank texture is unavailable: " + key);
        }
        return material;
    }

    private static void quad(
            BlockNeighborhood block,
            TileModelView target,
            Direction direction,
            int material
    ) {
        switch (direction) {
            case DOWN -> quad(block, target, direction,
                    0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 1F, 0F, 0F, 1F,
                    material);
            case UP -> quad(block, target, direction,
                    0F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 0F, 0F, 1F, 0F,
                    material);
            case NORTH -> quad(block, target, direction,
                    1F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F,
                    material);
            case SOUTH -> quad(block, target, direction,
                    0F, 0F, 1F, 1F, 0F, 1F, 1F, 1F, 1F, 0F, 1F, 1F,
                    material);
            case WEST -> quad(block, target, direction,
                    0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 0F,
                    material);
            case EAST -> quad(block, target, direction,
                    1F, 0F, 1F, 1F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 1F,
                    material);
        }
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private static void quad(
            BlockNeighborhood block,
            TileModelView target,
            Direction direction,
            float ax,
            float ay,
            float az,
            float bx,
            float by,
            float bz,
            float cx,
            float cy,
            float cz,
            float dx,
            float dy,
            float dz,
            int material
    ) {
        int start = target.add(2);
        TileModel model = target.getTileModel();
        model.setPositions(start, ax, ay, az, bx, by, bz, cx, cy, cz);
        model.setPositions(start + 1, ax, ay, az, cx, cy, cz, dx, dy, dz);
        model.setUvs(start, 0F, 0F, 0F, 1F, 1F, 1F);
        model.setUvs(start + 1, 0F, 0F, 1F, 1F, 1F, 0F);
        model.setMaterialIndex(start, material);
        model.setMaterialIndex(start + 1, material);
        model.setColor(start, 1F, 1F, 1F);
        model.setColor(start + 1, 1F, 1F, 1F);
        model.setAOs(start, 1F, 1F, 1F);
        model.setAOs(start + 1, 1F, 1F, 1F);

        LightSample light = sampleLight(block, direction);
        model.setSunlight(start, light.sunlight());
        model.setSunlight(start + 1, light.sunlight());
        model.setBlocklight(start, light.blocklight());
        model.setBlocklight(start + 1, light.blocklight());
    }

    private static LightSample sampleLight(BlockNeighborhood block, Direction direction) {
        LightData own = block.getLightData();
        LightData faced = block.getNeighborBlock(
                direction.toVector().getX(),
                direction.toVector().getY(),
                direction.toVector().getZ()
        ).getLightData();
        return new LightSample(
                Math.max(own.getSkyLight(), faced.getSkyLight()),
                Math.max(own.getBlockLight(), faced.getBlockLight())
        );
    }

    private record LightSample(int sunlight, int blocklight) {
    }
}
