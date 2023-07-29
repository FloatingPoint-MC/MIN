package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.ItemOverrideCache;

public class ItemOverrideList
{
    public static final ItemOverrideList NONE = new ItemOverrideList();
    private final List<ItemOverride> overrides = Lists.newArrayList();
    private ItemOverrideCache itemOverrideCache;

    private ItemOverrideList()
    {
    }

    public ItemOverrideList(List<ItemOverride> overridesIn)
    {
        for (int i = overridesIn.size() - 1; i >= 0; --i)
        {
            this.overrides.add(overridesIn.get(i));
        }

        if (this.overrides.size() > 65)
        {
            this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
        }
    }

    @Nullable
    public ResourceLocation applyOverride(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
    {
        if (!this.overrides.isEmpty())
        {
            if (this.itemOverrideCache != null)
            {
                ResourceLocation resourcelocation = this.itemOverrideCache.getModelLocation(stack, worldIn, entityIn);

                if (resourcelocation != null)
                {
                    return resourcelocation == ItemOverrideCache.LOCATION_NULL ? null : resourcelocation;
                }
            }

            for (ItemOverride itemoverride : this.overrides)
            {
                if (itemoverride.matchesItemStack(stack, worldIn, entityIn))
                {
                    if (this.itemOverrideCache != null)
                    {
                        this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, itemoverride.getLocation());
                    }

                    return itemoverride.getLocation();
                }
            }

            if (this.itemOverrideCache != null)
            {
                this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, ItemOverrideCache.LOCATION_NULL);
            }
        }

        return null;
    }

    public ImmutableList<ItemOverride> getOverrides()
    {
        return ImmutableList.copyOf(this.overrides);
    }
}
