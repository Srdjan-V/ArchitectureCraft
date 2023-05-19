package com.tridevmc.architecture.legacy.client.render.model;

import com.mojang.math.Transformation;
import com.tridevmc.architecture.client.render.model.resolver.IQuadMetadataResolver;
import com.tridevmc.architecture.legacy.client.render.model.data.LegacyArchitectureModelDataQuads;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@Deprecated
public interface LegacyIArchitectureModel<T> {

    default LegacyArchitectureModelDataQuads getQuads(LevelAccessor level, BlockPos pos, BlockState state) {
        return this.getQuads(level, pos, state, Transformation.identity());
    }

    LegacyArchitectureModelDataQuads getQuads(LevelAccessor level, BlockPos pos, BlockState state, Transformation transform);

    IQuadMetadataResolver<T> getMetadataResolver(LevelAccessor level, BlockPos pos, BlockState state);

    TextureAtlasSprite getDefaultSprite();

    List<BakedQuad> getDefaultModel();

}
