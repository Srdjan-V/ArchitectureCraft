package com.tridevmc.architecture.client.render.model.loader;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.tridevmc.architecture.common.shape.EnumShape;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.Map;

public class ArchitectureShapeModelLoader implements IGeometryLoader<ArchitectureModelGeometry>, ResourceManagerReloadListener {

    private final Map<EnumShape, ArchitectureModelGeometry> models = Maps.newHashMap();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.models.clear();
    }

    @Override
    public ArchitectureModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        var shapeName = modelContents.get("shapeName").getAsString();
        var shape = EnumShape.byName(shapeName);
        return null;
    }

}
