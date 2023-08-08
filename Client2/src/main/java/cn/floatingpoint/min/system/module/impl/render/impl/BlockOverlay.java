package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.DecimalValue;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.module.value.impl.PaletteValue;
import cn.floatingpoint.min.utils.client.Pair;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 13:45:54
 */
public class BlockOverlay extends RenderModule {
    private final OptionValue fill = new OptionValue(true);
    private final OptionValue fillChroma = new OptionValue(false, fill::getValue);
    private final PaletteValue fillColor = new PaletteValue(new Color(255, 255, 255, 50).getRGB(), () -> fill.getValue() && !fillChroma.getValue());
    private final IntegerValue fillChromaOpacity = new IntegerValue(1, 255, 1, 255, () -> fill.getValue() && fillChroma.getValue());
    private final OptionValue outline = new OptionValue(true);
    private final DecimalValue outlineWidth = new DecimalValue(0.2, 15.0, 0.1, 1.0, outline::getValue);
    private final OptionValue outlineChroma = new OptionValue(false, outline::getValue);
    private final PaletteValue outlineColor = new PaletteValue(new Color(255, 255, 255).getRGB(), () -> outline.getValue() && !outlineChroma.getValue());
    private final IntegerValue outlineChromaOpacity = new IntegerValue(1, 255, 1, 255, () -> outline.getValue() && outlineChroma.getValue());
    private final OptionValue throughBlock = new OptionValue(true, outline::getValue);

    public BlockOverlay() {
        addValues(
                new Pair<>("Fill", fill),
                new Pair<>("FillChroma", fillChroma),
                new Pair<>("FillColor", fillColor),
                new Pair<>("FillChromaOpacity", fillChromaOpacity),
                new Pair<>("Outline", outline),
                new Pair<>("OutlineWidth", outlineWidth),
                new Pair<>("OutlineChroma", outlineChroma),
                new Pair<>("OutlineColor", outlineColor),
                new Pair<>("OutlineChromaOpacity", outlineChromaOpacity),
                new Pair<>("ThroughBlock", throughBlock));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            assert pos != null;
            IBlockState state = mc.world.getBlockState(pos);
            double x = pos.getX() - mc.getRenderManager().getRenderPosX();
            double y = pos.getY() - mc.getRenderManager().getRenderPosY();
            double z = pos.getZ() - mc.getRenderManager().getRenderPosZ();
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            if (throughBlock.getValue()) {
                GL11.glDisable(2929);
            }
            GL11.glDepthMask(false);
            AxisAlignedBB blockBoundingBox = state.getBoundingBox(mc.world, pos);
            double minX = blockBoundingBox.minX;
            double maxX = blockBoundingBox.maxX;
            double minY = blockBoundingBox.minY;
            double maxY = blockBoundingBox.maxY;
            double minZ = blockBoundingBox.minZ;
            double maxZ = blockBoundingBox.maxZ;
            if (fill.getValue()) {
                int fillColor;
                if (fillChroma.getValue()) {
                    fillColor = RenderUtil.reAlpha(Color.getHSBColor((System.currentTimeMillis() % 3000) / 3000F, 0.8F, 1F).getRGB(), fillChromaOpacity.getValue());
                } else {
                    fillColor = this.fillColor.getValue();
                }
                Color color = new Color(fillColor, true);
                GL11.glPushMatrix();
                GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x + minX - 0.005, y + minY - 0.005, z + minZ - 0.005, x + maxX + 0.005, y + maxY + 0.005, z + maxZ + 0.005));
                GL11.glPopMatrix();
            }
            if (outline.getValue()) {
                int outlineColor;
                if (outlineChroma.getValue()) {
                    outlineColor = RenderUtil.reAlpha(Color.getHSBColor((System.currentTimeMillis() % 3000) / 3000F, 0.8F, 1F).getRGB(), outlineChromaOpacity.getValue());
                } else {
                    outlineColor = this.outlineColor.getValue();
                }
                Color color = new Color(outlineColor, true);
                GL11.glPushMatrix();
                GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
                GL11.glLineWidth(outlineWidth.getValue().floatValue());
                RenderUtil.drawBoundingBoxOutline(new AxisAlignedBB(x + minX - 0.005, y + minY - 0.005, z + minZ - 0.005, x + maxX + 0.005, y + maxY + 0.005, z + maxZ + 0.005));
                GL11.glPopMatrix();
            }
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            if (throughBlock.getValue()) {
                GL11.glEnable(2929);
            }
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
            GL11.glLineWidth(1.0F);
        }
    }
}
