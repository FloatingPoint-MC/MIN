package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

public interface IImageBuffer
{
    @Nullable
    BufferedImage parseUserSkin(@Nullable BufferedImage image);

    void skinAvailable();
}
