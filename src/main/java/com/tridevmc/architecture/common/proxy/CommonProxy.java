/*
 * MIT License
 *
 * Copyright (c) 2017 Benjamin K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tridevmc.architecture.common.proxy;

import com.google.common.collect.Maps;
import com.tridevmc.architecture.common.ArchitectureMod;
import com.tridevmc.architecture.core.ArchitectureLog;
import com.tridevmc.architecture.legacy.client.render.model.objson.LegacyOBJSON;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;

public class CommonProxy {

    private final Map<ResourceLocation, LegacyOBJSON> modelCache = Maps.newHashMap();

    public void setup(FMLCommonSetupEvent e) {

    }

    public void registerHandlers() {
        NeoForge.EVENT_BUS.register(this);
    }

    public void registerCustomRenderers() {
    }

    public LegacyOBJSON getCachedOBJSON(String name) {
        ResourceLocation loc = this.modelLocation(name);
        LegacyOBJSON model = this.modelCache.get(loc);
        if (model == null) {
            long t0 = System.nanoTime();
            model = LegacyOBJSON.fromResource(loc);
            this.modelCache.put(loc, model);
            long t1 = System.nanoTime();
            String msg = String.format("Loaded and cached '%s' in %s nanos.", name, t1 - t0);
            StartupNotificationManager.addModMessage(msg);
            ArchitectureLog.info(msg);
        }
        model.getVoxelized();
        return model;
    }

    public ResourceLocation modelLocation(String path) {
        return new ResourceLocation(ArchitectureMod.MOD_ID, path);
    }
}
