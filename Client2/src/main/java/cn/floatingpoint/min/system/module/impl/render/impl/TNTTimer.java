package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import net.minecraft.entity.item.EntityTNTPrimed;

import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:40:39
 */
public class TNTTimer extends RenderModule {
    public static final HashSet<EntityTNTPrimed> tntSet = new HashSet<>();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    @SuppressWarnings("all")
    public void onRender3D() {
        for (EntityTNTPrimed tnt : (HashSet<EntityTNTPrimed>) tntSet.clone()) {
            if (tnt.isDead) {
                tntSet.remove(tnt);
                continue;
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String text = decimalFormat.format((tnt.getFuse() / 20.0F));
            tnt.setCustomNameTag(text);
            tnt.setAlwaysRenderNameTag(true);
        }
    }
}
