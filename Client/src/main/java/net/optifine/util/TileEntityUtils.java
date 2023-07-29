package net.optifine.util;

import net.optifine.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;

public class TileEntityUtils {
    public static String getTileEntityName(IBlockAccess blockAccess, BlockPos blockPos) {
        TileEntity tileentity = blockAccess.getTileEntity(blockPos);
        return getTileEntityName(tileentity);
    }

    public static String getTileEntityName(TileEntity te) {
        if (!(te instanceof IWorldNameable)) {
            return null;
        } else {
            IWorldNameable iworldnameable = (IWorldNameable) te;
            updateTileEntityName(te);
            return !iworldnameable.hasCustomName() ? null : iworldnameable.getName();
        }
    }

    public static void updateTileEntityName(TileEntity te) {
        BlockPos blockpos = te.getPos();
        String s = getTileEntityRawName(te);

        if (s == null) {
            String s1 = getServerTileEntityRawName(blockpos);
            s1 = Config.normalize(s1);
            setTileEntityRawName(te, s1);
        }
    }

    public static String getServerTileEntityRawName(BlockPos blockPos) {
        TileEntity tileentity = IntegratedServerUtils.getTileEntity(blockPos);
        return tileentity == null ? null : getTileEntityRawName(tileentity);
    }

    public static String getTileEntityRawName(TileEntity te) {
        if (te instanceof TileEntityBeacon) {
            return ((TileEntityBeacon) te).getCustomName();
        } else if (te instanceof TileEntityBrewingStand) {
            return ((TileEntityBrewingStand) te).getCustomName();
        } else if (te instanceof TileEntityEnchantmentTable) {
            return ((TileEntityEnchantmentTable) te).getCustomName();
        } else if (te instanceof TileEntityFurnace) {
            return ((TileEntityFurnace) te).getCustomName();
        } else {
            return te instanceof TileEntityLockableLoot ? ((TileEntityLockableLoot) te).getCustomName() : null;
        }
    }

    public static void setTileEntityRawName(TileEntity te, String name) {
        if (te instanceof TileEntityBeacon) {
            ((TileEntityBeacon) te).setCustomName(name);
        } else if (te instanceof TileEntityBrewingStand) {
            ((TileEntityBrewingStand) te).setCustomName(name);
        } else if (te instanceof TileEntityEnchantmentTable) {
            ((TileEntityEnchantmentTable) te).setCustomName(name);
        } else if (te instanceof TileEntityFurnace) {
            ((TileEntityFurnace) te).setCustomName(name);
        } else if (te instanceof TileEntityLockableLoot) {
            ((TileEntityLockableLoot) te).setCustomName(name);
        }
    }
}
