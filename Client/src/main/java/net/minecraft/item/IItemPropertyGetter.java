package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IItemPropertyGetter
{
    float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn);
}
