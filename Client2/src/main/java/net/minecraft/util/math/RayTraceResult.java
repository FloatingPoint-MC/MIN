package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class RayTraceResult {
    @Nullable
    private BlockPos blockPos;

    /**
     * The type of hit that occurred, see {@link RayTraceResult.Type} for possibilities.
     */
    public Type typeOfHit;
    @Nullable
    public EnumFacing sideHit;

    /**
     * The vector position of the hit
     */
    public Vec3d hitVec;

    /**
     * The hit entity
     */
    @Nullable
    public Entity entityHit;

    public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
        this(Type.BLOCK, hitVecIn, sideHitIn, blockPosIn);
    }

    public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn) {
        this(Type.BLOCK, hitVecIn, sideHitIn, BlockPos.ORIGIN);
    }

    public RayTraceResult(Entity entityIn) {
        this(entityIn, new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ));
    }

    public RayTraceResult(Type typeIn, Vec3d hitVecIn, @Nullable EnumFacing sideHitIn, BlockPos blockPosIn) {
        this.typeOfHit = typeIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3d(hitVecIn.x, hitVecIn.y, hitVecIn.z);
    }

    public RayTraceResult(Entity entityHitIn, Vec3d hitVecIn) {
        this.typeOfHit = Type.ENTITY;
        this.entityHit = entityHitIn;
        this.hitVec = hitVecIn;
    }

    @Nullable
    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public enum Type {
        MISS,
        BLOCK,
        ENTITY
    }
}
