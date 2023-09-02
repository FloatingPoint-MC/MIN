package cn.floatingpoint.min.system.hyt.world;

import com.google.common.collect.Maps;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HYTChunkLoader
        implements IChunkLoader,
        IThreadedFileIO {
    private final Map<ChunkPos, NBTTagCompound> chunks;
    private final DataFixer dataFixer;
    private final Set<ChunkPos> chunkPosSet;
    private static final Logger logger = LogManager.getLogger();
    public final File file;

    @Nullable
    public static Entity createEntity(NBTTagCompound nBTTagCompound, World world) {
        try {
            return EntityList.createEntityFromNBT(nBTTagCompound, world);
        } catch (RuntimeException runtimeException) {
            return null;
        }
    }

    private void addNBTTag(Chunk chunk, World world, NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2;
        int n;
        nBTTagCompound.setInteger("xPos", chunk.x);
        nBTTagCompound.setInteger("zPos", chunk.z);
        nBTTagCompound.setLong("LastUpdate", world.getTotalWorldTime());
        nBTTagCompound.setIntArray("HeightMap", chunk.getHeightMap());
        nBTTagCompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated());
        nBTTagCompound.setBoolean("LightPopulated", chunk.isLightPopulated());
        nBTTagCompound.setLong("InhabitedTime", chunk.getInhabitedTime());
        NBTTagList nBTTagList2 = new NBTTagList();
        boolean bl = world.provider.hasSkyLight();
        ExtendedBlockStorage[] blockStorageArray = chunk.getBlockStorageArray();
        int n2 = blockStorageArray.length;
        int n3 = n = 0;
        while (n3 < n2) {
            ExtendedBlockStorage extendedBlockStorage = blockStorageArray[n];
            if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
                NBTTagList nBTTagList4;
                NBTTagCompound nBTTagCompound6 = nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound6.setByte("Y", (byte) (extendedBlockStorage.getYLocation() >> 4 & 0xFF));
                NibbleArray dataArray = new NibbleArray();
                byte[] data = new byte[4096];
                NibbleArray nibbleArray = extendedBlockStorage.getData().getDataForNBT(data, dataArray);
                nBTTagCompound6.setByteArray("Blocks", data);
                nBTTagCompound6.setByteArray("Data", dataArray.getData());
                if (nibbleArray != null) {
                    nBTTagCompound2.setByteArray("Add", nibbleArray.getData());
                }
                nBTTagCompound2.setByteArray("BlockLight", extendedBlockStorage.getBlockLight().getData());
                if (bl) {
                    nBTTagList4 = nBTTagList2;
                    nBTTagCompound2.setByteArray("SkyLight", extendedBlockStorage.getSkyLight().getData());
                } else {
                    nBTTagCompound2.setByteArray("SkyLight", new byte[extendedBlockStorage.getBlockLight().getData().length]);
                    nBTTagList4 = nBTTagList2;
                }
                nBTTagList4.appendTag(nBTTagCompound2);
            }
            n3 = ++n;
        }
        nBTTagCompound.setTag("Sections", nBTTagList2);
        nBTTagCompound.setByteArray("Biomes", chunk.getBiomeArray());
        chunk.setHasEntities(false);
        NBTTagList nbtTagList = new NBTTagList();
        int n4 = n2 = 0;
        while (n4 < chunk.getEntityLists().length) {
            for (Entity entity : chunk.getEntityLists()[n2]) {
                nBTTagCompound2 = new NBTTagCompound();
                try {
                    if (!entity.writeToNBTOptional(nBTTagCompound2)) continue;
                    chunk.setHasEntities(true);
                    nbtTagList.appendTag(nBTTagCompound2);
                } catch (Exception ignored) {
                }
            }
            n4 = ++n2;
        }
        nBTTagCompound.setTag("Entities", nbtTagList);
        NBTTagList nBTTagList5 = new NBTTagList();
        for (TileEntity tileEntity : chunk.getTileEntityMap().values()) {
            try {
                nBTTagCompound2 = tileEntity.writeToNBT(new NBTTagCompound());
                nBTTagList5.appendTag(nBTTagCompound2);
            } catch (Exception ignored) {

            }
        }
        nBTTagCompound.setTag("TileEntities", nBTTagList5);
        List<NextTickListEntry> list = world.getPendingBlockUpdates(chunk, false);
        if (list != null) {
            long l = world.getTotalWorldTime();
            NBTTagList tagList = new NBTTagList();
            for (NextTickListEntry nextTickListEntry : list) {
                NBTTagCompound nBTTagCompound7 = new NBTTagCompound();
                ResourceLocation resourceLocation = Block.REGISTRY.getNameForObject(nextTickListEntry.getBlock());
                nBTTagCompound7.setString("i", resourceLocation.toString());
                nBTTagCompound7.setInteger("x", nextTickListEntry.position.getX());
                nBTTagCompound7.setInteger("y", nextTickListEntry.position.getY());
                nBTTagCompound7.setInteger("z", nextTickListEntry.position.getZ());
                nBTTagCompound7.setInteger("t", (int) (nextTickListEntry.scheduledTime - l));
                nBTTagCompound7.setInteger("p", nextTickListEntry.priority);
                tagList.appendTag(nBTTagCompound7);
            }
            nBTTagCompound.setTag("TileTicks", tagList);
        }
    }

    public void saveChunk(@Nonnull World world, @Nonnull Chunk chunk) throws MinecraftException {
        world.checkSessionLock();
        try {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            nBTTagCompound.setTag("Level", nBTTagCompound2);
            nBTTagCompound.setInteger("DataVersion", 1343);
            HYTChunkLoader HYTChunkLoader = this;
            HYTChunkLoader.addNBTTag(chunk, world, nBTTagCompound2);
            HYTChunkLoader.Method1644(chunk.getPos(), nBTTagCompound);
        } catch (Exception exception) {
            logger.error("Failed to save chunk", exception);
        }
    }

    public void Method1644(ChunkPos chunkPos, NBTTagCompound nBTTagCompound) {
        if (!this.chunkPosSet.contains(chunkPos)) {
            this.chunks.put(chunkPos, nBTTagCompound);
        }
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    public boolean isChunkGeneratedAt(int x, int z) {
        ChunkPos chunkPos = new ChunkPos(x, z);
        if (this.chunks.get(chunkPos) != null) {
            return true;
        }
        return HYTChunkManager.hasChunkGenerated(this.file, x, z);
    }

    @Nullable
    public Object[] Method1647(World world, int n, int n2, NBTTagCompound nBTTagCompound) {
        NBTTagList nBTTagList;
        if (!nBTTagCompound.hasKey("Level", 10)) {
            logger.error("Chunk file at {},{} is missing level data, skipping", n, n2);
            return null;
        }
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Level");
        if (!nBTTagCompound2.hasKey("Sections", 9)) {
            logger.error("Chunk file at {},{} is missing block data, skipping", n, n2);
            return null;
        }
        Chunk chunk = this.readWorldNBT(world, nBTTagCompound2);
        if (!chunk.isAtLocation(n, n2)) {
            logger.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", n, n2, n, n2, chunk.x, chunk.z);
            nBTTagCompound2.setInteger("xPos", n);
            nBTTagCompound2.setInteger("zPos", n2);
            nBTTagList = nBTTagCompound2.getTagList("TileEntities", 10);
            int n3;
            int n4 = n3 = 0;
            while (n4 < nBTTagList.tagCount()) {
                NBTTagCompound nBTTagCompound4;
                NBTTagCompound nBTTagCompound5 = nBTTagCompound4 = nBTTagList.getCompoundTagAt(n3);
                nBTTagCompound5.setInteger("x", n * 16 + (nBTTagCompound5.getInteger("x") - chunk.x * 16));
                NBTTagCompound nBTTagCompound6 = nBTTagCompound4;
                nBTTagCompound6.setInteger("z", n2 * 16 + (nBTTagCompound6.getInteger("z") - chunk.z * 16));
                n4 = ++n3;
            }
            chunk = this.readWorldNBT(world, nBTTagCompound2);
        }
        Object[] objects = new Object[2];
        objects[0] = chunk;
        objects[1] = nBTTagCompound;
        return objects;
    }

    public void Method1648(World world, NBTTagCompound nBTTagCompound, Chunk chunk) {
        TileEntity tileEntity;
        int n;
        int n2;
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Entities", 10);
        int n3 = n2 = 0;
        while (n3 < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n2);
            HYTChunkLoader.Method1662(nBTTagCompound2, world, chunk);
            chunk.setHasEntities(true);
            n3 = ++n2;
        }
        NBTTagList nBTTagList2 = nBTTagCompound.getTagList("TileEntities", 10);
        int n4 = n = 0;
        while (n4 < nBTTagList2.tagCount()) {
            NBTTagCompound nBTTagCompound3 = nBTTagList2.getCompoundTagAt(n);
            tileEntity = TileEntity.create(world, nBTTagCompound3);
            if (tileEntity != null) {
                chunk.addTileEntity(tileEntity);
            }
            n4 = ++n;
        }
        if (nBTTagCompound.hasKey("TileTicks", 9)) {
            int n5;
            NBTTagList nBTTagList3 = nBTTagCompound.getTagList("TileTicks", 10);
            int n6 = n5 = 0;
            while (n6 < nBTTagList3.tagCount()) {
                World world2;
                Block block;
                NBTTagCompound tileEntity2 = nBTTagList3.getCompoundTagAt(n5);
                if (tileEntity2.hasKey("i", 8)) {
                    block = Block.getBlockFromName(tileEntity2.getString("i"));
                } else {
                    block = Block.getBlockById(tileEntity2.getInteger("i"));
                }
                world2 = world;
                assert block != null;
                world2.scheduleBlockUpdate(new BlockPos(tileEntity2.getInteger("x"), tileEntity2.getInteger("y"), tileEntity2.getInteger("z")), block, tileEntity2.getInteger("t"), tileEntity2.getInteger("p"));
                n6 = ++n5;
            }
        }
    }

    @Nullable
    public Object[] Method1649(World world, int n, int n2) throws IOException {
        ChunkPos chunkPos = new ChunkPos(n, n2);
        NBTTagCompound nBTTagCompound = this.chunks.get(chunkPos);
        if (nBTTagCompound == null) {
            DataInputStream dataInputStream = HYTChunkManager.getChunkFileInputStream(this.file, n, n2);
            if (dataInputStream == null) {
                return null;
            }
            nBTTagCompound = this.dataFixer.process(FixTypes.CHUNK, CompressedStreamTools.read(dataInputStream));
            dataInputStream.close();
        }
        return this.Method1647(world, n, n2, nBTTagCompound);
    }

    private Chunk readWorldNBT(World world, NBTTagCompound nBTTagCompound) {
        int n;
        int n2 = nBTTagCompound.getInteger("xPos");
        int n3 = nBTTagCompound.getInteger("zPos");
        Chunk chunk = new Chunk(world, n2, n3);
        chunk.setHeightMap(nBTTagCompound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(nBTTagCompound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(nBTTagCompound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(nBTTagCompound.getLong("InhabitedTime"));
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Sections", 10);
        ExtendedBlockStorage[] extendedBlockStorageArray = new ExtendedBlockStorage[16];
        boolean bl = world.provider.hasSkyLight();
        int n5 = n = 0;
        while (n5 < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound5 = nBTTagList.getCompoundTagAt(n);
            byte by = nBTTagCompound5.getByte("Y");
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(by << 4, bl);
            byte[] byArray = nBTTagCompound5.getByteArray("Blocks");
            NibbleArray nibbleArray = new NibbleArray(nBTTagCompound5.getByteArray("Data"));
            NibbleArray nibbleArray2 = nBTTagCompound5.hasKey("Add", 7) ? new NibbleArray(nBTTagCompound5.getByteArray("Add")) : null;
            extendedBlockStorage.getData().setDataFromNBT(byArray, nibbleArray, nibbleArray2);
            extendedBlockStorage.setBlockLight(new NibbleArray(nBTTagCompound5.getByteArray("BlockLight")));
            if (bl) {
                extendedBlockStorage.setBlockLight(new NibbleArray(nBTTagCompound5.getByteArray("SkyLight")));
            }
            extendedBlockStorage.recalculateRefCounts();
            extendedBlockStorageArray[by] = extendedBlockStorage;
            n5 = ++n;
        }
        chunk.setStorageArrays(extendedBlockStorageArray);
        if (nBTTagCompound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(nBTTagCompound.getByteArray("Biomes"));
        }
        return chunk;
    }

    @Nullable
    private static Entity giveRidingStatus(NBTTagCompound nBTTagCompound, World world, double d, double d2, double d3, boolean bl) {
        Entity entity = HYTChunkLoader.createEntity(nBTTagCompound, world);
        if (entity == null) {
            return null;
        }
        entity.setLocationAndAngles(d, d2, d3, entity.rotationYaw, entity.rotationPitch);
        if (bl && !world.spawnEntity(entity)) {
            return null;
        }
        if (nBTTagCompound.hasKey("Passengers", 9)) {
            int n;
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Passengers", 10);
            int n2 = n = 0;
            while (n2 < nBTTagList.tagCount()) {
                Entity entity3 = giveRidingStatus(nBTTagList.getCompoundTagAt(n), world, d, d2, d3, bl);
                if (entity3 != null) {
                    entity3.startRiding(entity, true);
                }
                n2 = ++n;
            }
        }
        return entity;
    }

    @Nullable
    private static Entity giveRidingStatus(NBTTagCompound nBTTagCompound, World world, boolean bl) {
        Entity entity = HYTChunkLoader.createEntity(nBTTagCompound, world);
        if (entity == null) {
            return null;
        }
        if (bl && !world.spawnEntity(entity)) {
            return null;
        }
        if (nBTTagCompound.hasKey("Passengers", 9)) {
            int n;
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Passengers", 10);
            int n2 = n = 0;
            while (n2 < nBTTagList.tagCount()) {
                Entity entity2 = giveRidingStatus(nBTTagList.getCompoundTagAt(n), world, bl);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                }
                n2 = ++n;
            }
        }
        return entity;
    }

    public boolean writeNextIO() {
        if (chunks.isEmpty()) {
            return false;
        } else {
            ChunkPos a1 = chunks.keySet().iterator().next();
            try {
                chunkPosSet.add(a1);
            } catch (Throwable var9) {
                chunkPosSet.remove(a1);
                throw var9;
            }

            chunkPosSet.remove(a1);
            return true;
        }
    }

    public void flush() {
        while (this.writeNextIO()) {
        }
    }

    public HYTChunkLoader(File file, DataFixer dataFixer) {
        this.chunks = Maps.newConcurrentMap();
        this.chunkPosSet = Collections.newSetFromMap(Maps.newConcurrentMap());
        this.file = file;
        this.dataFixer = dataFixer;
    }

    public void saveExtraChunkData(@Nonnull World world, @Nonnull Chunk chunk) {
    }

    @Nullable
    public Chunk loadChunk(@Nonnull World world, int n, int n2) throws IOException {
        Object[] objectArray = this.Method1649(world, n, n2);
        if (objectArray != null) {
            Chunk chunk = (Chunk) objectArray[0];
            NBTTagCompound nBTTagCompound = (NBTTagCompound) objectArray[1];
            this.Method1648(world, nBTTagCompound.getCompoundTag("Level"), chunk);
            return chunk;
        }
        return null;
    }

    public void chunkTick() {
    }

    @Nullable
    public static Entity Method1662(NBTTagCompound nBTTagCompound, World world, Chunk chunk) {
        Entity entity = HYTChunkLoader.createEntity(nBTTagCompound, world);
        if (entity == null) {
            return null;
        }
        chunk.addEntity(entity);
        if (nBTTagCompound.hasKey("Passengers", 9)) {
            int n;
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Passengers", 10);
            int n2 = n = 0;
            while (n2 < nBTTagList.tagCount()) {
                Entity entity2 = HYTChunkLoader.Method1662(nBTTagList.getCompoundTagAt(n), world, chunk);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                }
                n2 = ++n;
            }
        }
        return entity;
    }
}

