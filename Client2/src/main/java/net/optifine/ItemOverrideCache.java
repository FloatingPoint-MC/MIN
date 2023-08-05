package net.optifine;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.util.CompoundKey;

public class ItemOverrideCache {
    private final ItemOverrideProperty[] itemOverrideProperties;
    private final Map<CompoundKey, ResourceLocation> mapValueModels = new HashMap<CompoundKey, ResourceLocation>();
    public static final ResourceLocation LOCATION_NULL = new ResourceLocation("");

    public ItemOverrideCache(ItemOverrideProperty[] itemOverrideProperties) {
        this.itemOverrideProperties = itemOverrideProperties;
    }

    public ResourceLocation getModelLocation(ItemStack stack, World world, EntityLivingBase entity) {
        CompoundKey compoundkey = this.getValueKey(stack, world, entity);
        return compoundkey == null ? null : this.mapValueModels.get(compoundkey);
    }

    public void putModelLocation(ItemStack stack, World world, EntityLivingBase entity, ResourceLocation location) {
        CompoundKey compoundkey = this.getValueKey(stack, world, entity);

        if (compoundkey != null) {
            this.mapValueModels.put(compoundkey, location);
        }
    }

    private CompoundKey getValueKey(ItemStack stack, World world, EntityLivingBase entity) {
        Integer[] ainteger = new Integer[this.itemOverrideProperties.length];

        for (int i = 0; i < ainteger.length; ++i) {
            Integer integer = this.itemOverrideProperties[i].getValueIndex(stack, world, entity);

            if (integer == null) {
                return null;
            }

            ainteger[i] = integer;
        }

        return new CompoundKey(ainteger);
    }

    public String toString() {
        return "properties: " + this.itemOverrideProperties.length + ", models: " + this.mapValueModels.size();
    }
}
