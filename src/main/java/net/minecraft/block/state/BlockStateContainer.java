package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.optifine.model.BlockModelUtils;

public class BlockStateContainer {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> GET_NAME_FUNC = new Function<IProperty<?>, String>() {
        @Nullable
        public String apply(@Nullable IProperty<?> p_apply_1_) {
            return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
        }
    };
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;

    protected BlockStateContainer.StateImplementation createState(Block p_createState_1_, ImmutableMap<IProperty<?>, Comparable<?>> p_createState_2_) {
        return new BlockStateContainer.StateImplementation(p_createState_1_, p_createState_2_);
    }

    public BlockStateContainer(Block p_i10_1_, IProperty<?>... p_i10_2_) {
        this.block = p_i10_1_;
        Map<String, IProperty<?>> map = Maps.newHashMap();

        for (IProperty<?> iproperty : p_i10_2_) {
            validateProperty(p_i10_1_, iproperty);
            map.put(iproperty.getName(), iproperty);
        }

        this.properties = ImmutableSortedMap.copyOf(map);
        Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map2 = Maps.newLinkedHashMap();
        List<BlockStateContainer.StateImplementation> list = Lists.newArrayList();

        for (List<Comparable<?>> list1 : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map<IProperty<?>, Comparable<?>> map1 = MapPopulator.createMap(this.properties.values(), list1);
            BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation = this.createState(p_i10_1_, ImmutableMap.copyOf(map1));
            map2.put(map1, blockstatecontainer$stateimplementation);
            list.add(blockstatecontainer$stateimplementation);
        }

        for (BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation1 : list) {
            blockstatecontainer$stateimplementation1.buildPropertyValueTable(map2);
        }

        this.validStates = ImmutableList.copyOf(list);
    }

    public static <T extends Comparable<T>> String validateProperty(Block block, IProperty<T> property) {
        String s = property.getName();

        if (!NAME_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        } else {
            for (T t : property.getAllowedValues()) {
                String s1 = property.getName(t);

                if (!NAME_PATTERN.matcher(s1).matches()) {
                    throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
                }
            }

            return s;
        }
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable<?>>> getAllowedValues() {
        List<Iterable<Comparable<?>>> list = Lists.newArrayList();
        ImmutableCollection<IProperty<?>> immutablecollection = this.properties.values();
        UnmodifiableIterator unmodifiableiterator = immutablecollection.iterator();

        while (unmodifiableiterator.hasNext()) {
            IProperty<?> iproperty = (IProperty) unmodifiableiterator.next();
            list.add(((IProperty) iproperty).getAllowedValues());
        }

        return list;
    }

    public IBlockState getBaseState() {
        return this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection<IProperty<?>> getProperties() {
        return this.properties.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties.values(), GET_NAME_FUNC)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }

    static class StateImplementation extends BlockStateBase {
        private final Block block;
        private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
        private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

        private StateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            this.block = blockIn;
            this.properties = propertiesIn;
        }

        protected StateImplementation(Block p_i9_1_, ImmutableMap<IProperty<?>, Comparable<?>> p_i9_2_, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> p_i9_3_) {
            this.block = p_i9_1_;
            this.properties = p_i9_2_;
            this.propertyValueTable = p_i9_3_;
        }

        public Collection<IProperty<?>> getPropertyKeys() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        public <T extends Comparable<T>> T getValue(IProperty<T> property) {
            Comparable<?> comparable = this.properties.get(property);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            } else {
                return property.getValueClass().cast(comparable);
            }
        }

        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
            Comparable<?> comparable = this.properties.get(property);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            } else if (comparable == value) {
                return this;
            } else {
                IBlockState iblockstate = this.propertyValueTable.get(property, value);

                if (iblockstate == null) {
                    throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value");
                } else {
                    return iblockstate;
                }
            }
        }

        public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
            return this.properties;
        }

        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object p_equals_1_) {
            return this == p_equals_1_;
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            } else {
                Table<IProperty<?>, Comparable<?>, IBlockState> table = HashBasedTable.create();
                UnmodifiableIterator unmodifiableiterator = this.properties.entrySet().iterator();

                while (unmodifiableiterator.hasNext()) {
                    Entry<IProperty<?>, Comparable<?>> entry = (Entry) unmodifiableiterator.next();
                    IProperty<?> iproperty = entry.getKey();

                    for (Comparable<?> comparable : iproperty.getAllowedValues()) {
                        if (comparable != entry.getValue()) {
                            table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                        }
                    }
                }

                this.propertyValueTable = ImmutableTable.copyOf(table);
            }
        }

        private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> property, Comparable<?> value) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newHashMap(this.properties);
            map.put(property, value);
            return map;
        }

        public Material getMaterial() {
            return this.block.getMaterial(this);
        }

        public boolean isFullBlock() {
            return this.block.isFullBlock(this);
        }

        public boolean canEntitySpawn(Entity entityIn) {
            return this.block.canEntitySpawn(this, entityIn);
        }

        public int getLightOpacity() {
            return this.block.getLightOpacity(this);
        }

        public int getLightValue() {
            return this.block.getLightValue(this);
        }

        public boolean isTranslucent() {
            return this.block.isTranslucent(this);
        }

        public boolean useNeighborBrightness() {
            return this.block.getUseNeighborBrightness(this);
        }

        public MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
            return this.block.getMapColor(this, p_185909_1_, p_185909_2_);
        }

        public IBlockState withRotation(Rotation rot) {
            return this.block.withRotation(this, rot);
        }

        public IBlockState withMirror(Mirror mirrorIn) {
            return this.block.withMirror(this, mirrorIn);
        }

        public boolean isFullCube() {
            return this.block.isFullCube(this);
        }

        public boolean hasCustomBreakingProgress() {
            return this.block.hasCustomBreakingProgress(this);
        }

        public EnumBlockRenderType getRenderType() {
            return this.block.getRenderType(this);
        }

        public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
            return this.block.getPackedLightmapCoords(this, source, pos);
        }

        public float getAmbientOcclusionLightValue() {
            return this.block.getAmbientOcclusionLightValue(this);
        }

        public boolean isBlockNormalCube() {
            return this.block.isBlockNormalCube(this);
        }

        public boolean isNormalCube() {
            return this.block.isNormalCube(this);
        }

        public boolean canProvidePower() {
            return this.block.canProvidePower(this);
        }

        public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            return this.block.getWeakPower(this, blockAccess, pos, side);
        }

        public boolean hasComparatorInputOverride() {
            return this.block.hasComparatorInputOverride(this);
        }

        public int getComparatorInputOverride(World worldIn, BlockPos pos) {
            return this.block.getComparatorInputOverride(this, worldIn, pos);
        }

        public float getBlockHardness(World worldIn, BlockPos pos) {
            return this.block.getBlockHardness(this, worldIn, pos);
        }

        public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
            return this.block.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
        }

        public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            return this.block.getStrongPower(this, blockAccess, pos, side);
        }

        public EnumPushReaction getPushReaction() {
            return this.block.getPushReaction(this);
        }

        public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
            return this.block.getActualState(this, blockAccess, pos);
        }

        public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
            return this.block.getSelectedBoundingBox(this, worldIn, pos);
        }

        public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
            return this.block.shouldSideBeRendered(this, blockAccess, pos, facing);
        }

        public boolean isOpaqueCube() {
            return this.block.isOpaqueCube(this);
        }

        @Nullable
        public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
            return this.block.getCollisionBoundingBox(this, worldIn, pos);
        }

        public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_) {
            this.block.addCollisionBoxToList(this, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
        }

        public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
            Block.EnumOffsetType block$enumoffsettype = this.block.getOffsetType();

            if (block$enumoffsettype != Block.EnumOffsetType.NONE && !(this.block instanceof BlockFlower)) {
                AxisAlignedBB axisalignedbb = this.block.getBoundingBox(this, blockAccess, pos);
                axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, pos);
                return axisalignedbb;
            } else {
                return this.block.getBoundingBox(this, blockAccess, pos);
            }
        }

        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
            return this.block.collisionRayTrace(this, worldIn, pos, start, end);
        }

        public boolean isTopSolid() {
            return this.block.isTopSolid(this);
        }

        public Vec3d getOffset(IBlockAccess access, BlockPos pos) {
            return this.block.getOffset(this, access, pos);
        }

        public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
            return this.block.eventReceived(this, worldIn, pos, id, param);
        }

        public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
            this.block.neighborChanged(this, worldIn, pos, blockIn, fromPos);
        }

        public boolean causesSuffocation() {
            return this.block.causesSuffocation(this);
        }

        public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
            return this.propertyValueTable;
        }

        public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
            return this.block.getBlockFaceShape(worldIn, this, pos, facing);
        }
    }
}
