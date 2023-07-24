package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IResourceManager
{
    Set<String> getResourceDomains();

    @Nullable
    IResource getResource(ResourceLocation location) throws IOException;

    List<IResource> getAllResources(ResourceLocation location) throws IOException;
}
