package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.optifine.Config;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager resourceManager;

    public TextureManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }

        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }

        ITextureObject itextureobject = this.mapTextureObjects.get(resource);

        if (EmissiveTextures.isActive()) {
            itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
        }

        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }

        if (Config.isShaders()) {
            ShadersTex.bindTexture(itextureobject);
        } else {
            TextureUtil.bindTexture(itextureobject.getGlTextureId());
        }

    }

    public void loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;

        try {
            textureObj.loadTexture(this.resourceManager);
        } catch (IOException ioexception) {
            if (textureLocation != RESOURCE_LOCATION_EMPTY) {
                LOGGER.warn("Failed to load texture: {}", textureLocation, ioexception);
            }

            textureObj = TextureUtil.MISSING_TEXTURE;
            this.mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        } catch (Throwable throwable) {
            final ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addDetail("Texture object class", () -> textureObjf.getClass().getName());
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    @Nullable
    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        if (name.equals("logo")) {
            texture = Config.getMojangLogoTexture(texture);
        }

        Integer integer = this.mapTextureCounters.get(name);

        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }

        this.mapTextureCounters.put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick() {
        for (ITickable itickable : this.listTickables) {
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);

        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();

        while (iterator.hasNext()) {
            ResourceLocation resourcelocation = iterator.next();
            String s = resourcelocation.getPath();

            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/") || EmissiveTextures.isEmissive(resourcelocation)) {
                ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);

                if (itextureobject instanceof AbstractTexture) {
                    AbstractTexture abstracttexture = (AbstractTexture) itextureobject;
                    abstracttexture.deleteGlTexture();
                }

                iterator.remove();
            }
        }

        EmissiveTextures.update();
        Set<Entry<ResourceLocation, ITextureObject>> set = new HashSet<>(this.mapTextureObjects.entrySet());
        Iterator<Entry<ResourceLocation, ITextureObject>> iterator1 = set.iterator();

        while (iterator1.hasNext()) {
            Entry<ResourceLocation, ITextureObject> entry = iterator1.next();
            ITextureObject textureObject = entry.getValue();

            if (textureObject == TextureUtil.MISSING_TEXTURE) {
                iterator1.remove();
            } else {
                this.loadTexture(entry.getKey(), textureObject);
            }
        }
    }

    public void reloadBannerTextures() {
        for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            ITextureObject itextureobject = entry.getValue();

            if (itextureobject instanceof LayeredColorMaskTexture) {
                this.loadTexture(resourcelocation, itextureobject);
            }
        }
    }
}
