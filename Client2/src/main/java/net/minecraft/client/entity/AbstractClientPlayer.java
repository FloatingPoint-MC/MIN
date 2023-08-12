package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.optifine.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.optifine.player.CapeUtils;
import net.optifine.player.PlayerConfigurations;

import java.util.Objects;

public abstract class AbstractClientPlayer extends EntityPlayer {
    private NetworkPlayerInfo playerInfo;
    public float rotateElytraX;
    public float rotateElytraY;
    public float rotateElytraZ;
    private ResourceLocation locationOfCape = null;
    private long reloadCapeTimeMs = 0L;
    private boolean elytraOfCape = false;
    private String nameClear;
    public EntityShoulderRiding entityShoulderLeft;
    public EntityShoulderRiding entityShoulderRight;

    public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
        this.nameClear = playerProfile.getName();

        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }

        CapeUtils.downloadCape(this);
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    /**
     * Returns true if the player is in spectator mode.
     */
    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.SPECTATOR;
    }

    public boolean isCreative() {
        NetworkPlayerInfo networkplayerinfo = Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.CREATIVE;
    }

    /**
     * Checks if this instance of AbstractClientPlayer has any associated player data.
     */
    public boolean hasPlayerInfo() {
        return this.getPlayerInfo() != null;
    }

    @Nullable
    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(this.getUniqueID());
        }

        return this.playerInfo;
    }

    /**
     * Returns true if the player has an associated skin.
     */
    public boolean hasSkin() {
        return this.getPlayerInfo() != null;
    }

    /**
     * Returns the ResourceLocation associated with the player's skin
     */
    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : networkplayerinfo.getLocationSkin();
    }

    @Nullable
    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        } else {
            if (this.reloadCapeTimeMs != 0L && System.currentTimeMillis() > this.reloadCapeTimeMs) {
                CapeUtils.reloadCape(this);
                this.reloadCapeTimeMs = 0L;
            }

            if (this.locationOfCape != null) {
                return this.locationOfCape;
            } else {
                NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
                return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
            }
        }
    }

    public boolean isPlayerInfoSet() {
        return this.getPlayerInfo() != null;
    }


    /**
     * Gets the special Elytra texture for the player.
     */
    @Nullable
    public ResourceLocation getLocationElytra() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : networkplayerinfo.getLocationElytra();
    }

    public static void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);

        if (itextureobject == null) {
            itextureobject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, itextureobject);
        }

    }

    /**
     * Returns True if the username has an associated skin.
     */
    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String getSkinType() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : networkplayerinfo.getSkinType();
    }

    public float getFovModifier() {
        float f = 1.0F;

        if (this.capabilities.isFlying) {
            f *= 1.1F;
        }

        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        f = (float) ((double) f * ((iattributeinstance.getAttributeValue() / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (this.isHandActive() && this.getActiveItemStack().getItem() instanceof ItemBow) {
            int i = this.getItemInUseMaxCount();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return f;
    }

    public String getNameClear() {
        return this.nameClear;
    }

    public void setLocationOfCape(ResourceLocation p_setLocationOfCape_1_) {
        this.locationOfCape = p_setLocationOfCape_1_;
    }

    public boolean hasElytraCape() {
        ResourceLocation resourcelocation = this.getLocationCape();

        if (resourcelocation == null) {
            return false;
        } else {
            return resourcelocation != this.locationOfCape || this.elytraOfCape;
        }
    }

    public void setElytraOfCape(boolean p_setElytraOfCape_1_) {
        this.elytraOfCape = p_setElytraOfCape_1_;
    }

    public void setReloadCapeTimeMs(long p_setReloadCapeTimeMs_1_) {
        this.reloadCapeTimeMs = p_setReloadCapeTimeMs_1_;
    }
}
