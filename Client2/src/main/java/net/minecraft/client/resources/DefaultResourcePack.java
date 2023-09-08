package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class DefaultResourcePack implements IResourcePack {
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of("minecraft", "realms");
    private final ResourceIndex resourceIndex;
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;

    public DefaultResourcePack(ResourceIndex resourceIndexIn) {
        this.resourceIndex = resourceIndexIn;
    }

    public InputStream getInputStream(ResourceLocation location) throws IOException {
        InputStream inputStream = this.getInputStreamAssets(location);

        if (inputStream != null) {
            return inputStream;
        } else {
            inputStream = this.getResourceStream(location);

            if (inputStream != null) {
                return inputStream;
            } else {
                throw new FileNotFoundException(location.getPath());
            }
        }
    }

    @Nullable
    public InputStream getInputStreamAssets(ResourceLocation location) throws IOException {
        File file1 = this.resourceIndex.getFile(location);
        return file1 != null && file1.isFile() ? Files.newInputStream(file1.toPath()) : null;
    }

    @Nullable
    public InputStream getResourceStream(ResourceLocation location) {
        String s = "/assets/" + location.getNamespace() + "/" + location.getPath();
        try {
            URL url = DefaultResourcePack.class.getResource(s);
            return url != null && this.validatePath(new File(url.getFile()), s) ? DefaultResourcePack.class.getResourceAsStream(s) : null;
        } catch (IOException e) {
            return DefaultResourcePack.class.getResourceAsStream(s);
        }
    }

    public boolean resourceExists(ResourceLocation location) {
        return this.getResourceStream(location) != null || this.resourceIndex.isFileExisting(location);
    }

    public Set<String> getResourceDomains() {
        return DEFAULT_RESOURCE_DOMAINS;
    }

    @Nullable
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) {
        try {
            InputStream inputstream = new FileInputStream(this.resourceIndex.getPackMcmeta());
            return AbstractResourcePack.readMetadata(metadataSerializer, inputstream, metadataSectionName);
        } catch (RuntimeException | FileNotFoundException e) {
            return null;
        }
    }

    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(Objects.requireNonNull(DefaultResourcePack.class.getResourceAsStream("/" + (new ResourceLocation("pack.png")).getPath())));
    }

    public String getPackName() {
        return "Default";
    }

    private boolean validatePath(File p_validatePath_1_, String p_validatePath_2_) throws IOException {
        String s = p_validatePath_1_.getPath();

        if (s.startsWith("file:")) {
            if (ON_WINDOWS) {
                s = s.replace("\\", "/");
            }

            return s.endsWith(p_validatePath_2_);
        } else {
            return FolderResourcePack.validatePath(p_validatePath_1_, p_validatePath_2_);
        }
    }
}
