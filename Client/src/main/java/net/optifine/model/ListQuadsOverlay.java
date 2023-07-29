package net.optifine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;

public class ListQuadsOverlay
{
    private final List<BakedQuad> listQuads = new ArrayList<BakedQuad>();
    private final List<IBlockState> listBlockStates = new ArrayList<IBlockState>();
    private final List<BakedQuad> listQuadsSingle = Collections.emptyList();

    public void addQuad(BakedQuad quad, IBlockState blockState)
    {
        if (quad != null)
        {
            this.listQuads.add(quad);
            this.listBlockStates.add(blockState);
        }
    }

    public int size()
    {
        return this.listQuads.size();
    }

    public BakedQuad getQuad(int index)
    {
        return this.listQuads.get(index);
    }

    public IBlockState getBlockState(int index)
    {
        return index >= 0 && index < this.listBlockStates.size() ? this.listBlockStates.get(index) : Blocks.AIR.getDefaultState();
    }

    public List<BakedQuad> getListQuadsSingle(BakedQuad quad)
    {
        this.listQuadsSingle.set(0, quad);
        return this.listQuadsSingle;
    }

    public void clear()
    {
        this.listQuads.clear();
        this.listBlockStates.clear();
    }
}
