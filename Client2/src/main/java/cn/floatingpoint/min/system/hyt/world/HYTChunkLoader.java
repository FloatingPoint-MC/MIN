package cn.floatingpoint.min.system.hyt.world;

import cn.floatingpoint.min.utils.client.Pair;
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
    public final String worldName;

    @Nullable
    public static Entity createEntity(NBTTagCompound nBTTagCompound, World world) {
        try {
            return EntityList.createEntityFromNBT(nBTTagCompound, world);
        } catch (RuntimeException runtimeException) {
            return null;
        }
    }

    private void addNBTTag(Chunk chunk, World world, NBTTagCompound tagCompound) {
        NBTTagCompound tagCompound1;
        tagCompound.setInteger("xPos", chunk.x);
        tagCompound.setInteger("zPos", chunk.z);
        tagCompound.setLong("LastUpdate", world.getTotalWorldTime());
        tagCompound.setIntArray("HeightMap", chunk.getHeightMap());
        tagCompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated());
        tagCompound.setBoolean("LightPopulated", chunk.isLightPopulated());
        tagCompound.setLong("InhabitedTime", chunk.getInhabitedTime());
        NBTTagList nbtTagList1 = new NBTTagList();
        boolean hasSkyLight = world.provider.hasSkyLight();
        ExtendedBlockStorage[] blockStorageArray = chunk.getBlockStorageArray();
        int i = 0;
        while (i < blockStorageArray.length) {
            ExtendedBlockStorage extendedBlockStorage = blockStorageArray[i];
            if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
                NBTTagList nbtTagList;
                tagCompound1 = new NBTTagCompound();
                tagCompound1.setByte("Y", (byte) (extendedBlockStorage.getYLocation() >> 4 & 0xFF));
                NibbleArray dataArray = new NibbleArray();
                byte[] data = new byte[4096];
                NibbleArray nibbleArray = extendedBlockStorage.getData().getDataForNBT(data, dataArray);
                tagCompound1.setByteArray("Blocks", data);
                tagCompound1.setByteArray("Data", dataArray.getData());
                if (nibbleArray != null) {
                    tagCompound1.setByteArray("Add", nibbleArray.getData());
                }
                tagCompound1.setByteArray("BlockLight", extendedBlockStorage.getBlockLight().getData());
                if (hasSkyLight) {
                    nbtTagList = nbtTagList1;
                    tagCompound1.setByteArray("SkyLight", extendedBlockStorage.getSkyLight().getData());
                } else {
                    tagCompound1.setByteArray("SkyLight", new byte[extendedBlockStorage.getBlockLight().getData().length]);
                    nbtTagList = nbtTagList1;
                }
                nbtTagList.appendTag(tagCompound1);
            }
            ++i;
        }
        tagCompound.setTag("Sections", nbtTagList1);
        tagCompound.setByteArray("Biomes", chunk.getBiomeArray());
        chunk.setHasEntities(false);
        NBTTagList nbtTagList = new NBTTagList();
        i = 0;
        while (i < chunk.getEntityLists().length) {
            for (Entity entity : chunk.getEntityLists()[i]) {
                tagCompound1 = new NBTTagCompound();
                try {
                    if (!entity.writeToNBTOptional(tagCompound1)) continue;
                    chunk.setHasEntities(true);
                    nbtTagList.appendTag(tagCompound1);
                } catch (Exception ignored) {
                }
            }
            ++i;
        }
        tagCompound.setTag("Entities", nbtTagList);
        NBTTagList nBTTagList5 = new NBTTagList();
        for (TileEntity tileEntity : chunk.getTileEntityMap().values()) {
            try {
                tagCompound1 = tileEntity.writeToNBT(new NBTTagCompound());
                nBTTagList5.appendTag(tagCompound1);
            } catch (Exception ignored) {

            }
        }
        tagCompound.setTag("TileEntities", nBTTagList5);
        List<NextTickListEntry> list = world.getPendingBlockUpdates(chunk, false);
        if (list != null) {
            long l = world.getTotalWorldTime();
            NBTTagList tagList = new NBTTagList();
            for (NextTickListEntry nextTickListEntry : list) {
                NBTTagCompound tagCompound2 = new NBTTagCompound();
                ResourceLocation resourceLocation = Block.REGISTRY.getNameForObject(nextTickListEntry.getBlock());
                tagCompound2.setString("i", resourceLocation.toString());
                tagCompound2.setInteger("x", nextTickListEntry.position.getX());
                tagCompound2.setInteger("y", nextTickListEntry.position.getY());
                tagCompound2.setInteger("z", nextTickListEntry.position.getZ());
                tagCompound2.setInteger("t", (int) (nextTickListEntry.scheduledTime - l));
                tagCompound2.setInteger("p", nextTickListEntry.priority);
                tagList.appendTag(tagCompound2);
            }
            tagCompound.setTag("TileTicks", tagList);
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
            HYTChunkLoader.saveChunk(chunk.getPos(), nBTTagCompound);
        } catch (Exception exception) {
            logger.error("Failed to save chunk", exception);
        }
    }

    public void saveChunk(ChunkPos chunkPos, NBTTagCompound tagCompound) {
        if (!this.chunkPosSet.contains(chunkPos)) {
            this.chunks.put(chunkPos, tagCompound);
        }
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    public boolean isChunkGeneratedAt(int x, int z) {
        ChunkPos chunkPos = new ChunkPos(x, z);
        if (this.chunks.get(chunkPos) != null) {
            return true;
        }
        return HYTChunkManager.hasChunkGenerated(this.worldName, x, z);
    }

    @Nullable
    public Pair<Chunk, NBTTagCompound> getChunkWithNBT(World world, int x, int z, NBTTagCompound nBTTagCompound) {
        NBTTagList nBTTagList;
        if (!nBTTagCompound.hasKey("Level", 10)) {
            logger.error("Chunk file at {},{} is missing level data, skipping", x, z);
            return null;
        }
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Level");
        if (!nBTTagCompound2.hasKey("Sections", 9)) {
            logger.error("Chunk file at {},{} is missing block data, skipping", x, z);
            return null;
        }
        Chunk chunk = this.readWorldNBT(world, nBTTagCompound2);
        if (!chunk.isAtLocation(x, z)) {
            logger.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", x, z, x, z, chunk.x, chunk.z);
            nBTTagCompound2.setInteger("xPos", x);
            nBTTagCompound2.setInteger("zPos", z);
            nBTTagList = nBTTagCompound2.getTagList("TileEntities", 10);
            int n3;
            int n4 = n3 = 0;
            while (n4 < nBTTagList.tagCount()) {
                NBTTagCompound nBTTagCompound4;
                NBTTagCompound nBTTagCompound5 = nBTTagCompound4 = nBTTagList.getCompoundTagAt(n3);
                nBTTagCompound5.setInteger("x", x * 16 + (nBTTagCompound5.getInteger("x") - chunk.x * 16));
                NBTTagCompound nBTTagCompound6 = nBTTagCompound4;
                nBTTagCompound6.setInteger("z", z * 16 + (nBTTagCompound6.getInteger("z") - chunk.z * 16));
                n4 = ++n3;
            }
            chunk = this.readWorldNBT(world, nBTTagCompound2);
        }
        return new Pair<>(chunk, nBTTagCompound);
    }

    public void loadEntitiesInChunks(World world, NBTTagCompound nbtTagCompound, Chunk chunk) {
        NBTTagList entities = nbtTagCompound.getTagList("Entities", 10);
        int i = 0;
        while (i < entities.tagCount()) {
            NBTTagCompound tagCompound = entities.getCompoundTagAt(i);
            HYTChunkLoader.initEntity(tagCompound, world, chunk);
            chunk.setHasEntities(true);
            ++i;
        }
        NBTTagList tileEntities = nbtTagCompound.getTagList("TileEntities", 10);
        i = 0;
        while (i < tileEntities.tagCount()) {
            NBTTagCompound tagCompound = tileEntities.getCompoundTagAt(i);
            TileEntity tileEntity = TileEntity.create(world, tagCompound);
            if (tileEntity != null) {
                chunk.addTileEntity(tileEntity);
            }
            ++i;
        }
        if (nbtTagCompound.hasKey("TileTicks", 9)) {
            NBTTagList tileTicks = nbtTagCompound.getTagList("TileTicks", 10);
            i = 0;
            while (i < tileTicks.tagCount()) {
                Block block;
                NBTTagCompound tagCompound = tileTicks.getCompoundTagAt(i);
                if (tagCompound.hasKey("i", 8)) {
                    block = Block.getBlockFromName(tagCompound.getString("i"));
                } else {
                    block = Block.getBlockById(tagCompound.getInteger("i"));
                }
                assert block != null;
                world.scheduleBlockUpdate(new BlockPos(tagCompound.getInteger("x"), tagCompound.getInteger("y"), tagCompound.getInteger("z")), block, tagCompound.getInteger("t"), tagCompound.getInteger("p"));
                ++i;
            }
        }
    }

    @Nullable
    public Pair<Chunk, NBTTagCompound> getValidChunkWithNBT(World world, int x, int z) throws IOException {
        ChunkPos chunkPos = new ChunkPos(x, z);
        NBTTagCompound tagCompound = this.chunks.get(chunkPos);
        if (tagCompound == null) {
            DataInputStream dataInputStream = HYTChunkManager.getChunkFileInputStream(this.worldName, x, z);
            if (dataInputStream == null) {
                return null;
            }
            tagCompound = this.dataFixer.process(FixTypes.CHUNK, CompressedStreamTools.read(dataInputStream));
            dataInputStream.close();
        }
        return this.getChunkWithNBT(world, x, z, tagCompound);
    }

    private Chunk readWorldNBT(World world, NBTTagCompound nbtTagCompound) {
        int xPos = nbtTagCompound.getInteger("xPos");
        int zPos = nbtTagCompound.getInteger("zPos");
        Chunk chunk = new Chunk(world, xPos, zPos);
        chunk.setHeightMap(nbtTagCompound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(nbtTagCompound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(nbtTagCompound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(nbtTagCompound.getLong("InhabitedTime"));
        NBTTagList sections = nbtTagCompound.getTagList("Sections", 10);
        ExtendedBlockStorage[] extendedBlockStorageArray = new ExtendedBlockStorage[16];
        boolean hasSkyLight = world.provider.hasSkyLight();
        int i = 0;
        while (i < sections.tagCount()) {
            NBTTagCompound tagCompound = sections.getCompoundTagAt(i);
            byte y = tagCompound.getByte("Y");
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(y << 4, hasSkyLight);
            byte[] byArray = tagCompound.getByteArray("Blocks");
            NibbleArray data = new NibbleArray(tagCompound.getByteArray("Data"));
            NibbleArray nibbleArray = tagCompound.hasKey("Add", 7) ? new NibbleArray(tagCompound.getByteArray("Add")) : null;
            extendedBlockStorage.getData().setDataFromNBT(byArray, data, nibbleArray);
            extendedBlockStorage.setBlockLight(new NibbleArray(tagCompound.getByteArray("BlockLight")));
            if (hasSkyLight) {
                extendedBlockStorage.setBlockLight(new NibbleArray(tagCompound.getByteArray("SkyLight")));
            }
            extendedBlockStorage.recalculateRefCounts();
            extendedBlockStorageArray[y] = extendedBlockStorage;
            ++i;
        }
        chunk.setStorageArrays(extendedBlockStorageArray);
        if (nbtTagCompound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(nbtTagCompound.getByteArray("Biomes"));
        }
        return chunk;
    }

    public boolean writeNextIO() {
        if (chunks.isEmpty()) {
            return false;
        } else {
            ChunkPos chunkPos = chunks.keySet().iterator().next();
            try {
                chunkPosSet.add(chunkPos);
            } catch (Throwable throwable) {
                chunkPosSet.remove(chunkPos);
                throw throwable;
            }

            chunkPosSet.remove(chunkPos);
            return true;
        }
    }

    public void flush() {
        while (writeNextIO()) {
            writeNextIO();
        }
    }

    public HYTChunkLoader(String worldName, DataFixer dataFixer) {
        this.chunks = Maps.newConcurrentMap();
        this.chunkPosSet = Collections.newSetFromMap(Maps.newConcurrentMap());
        this.worldName = worldName;
        this.dataFixer = dataFixer;
    }

    public void saveExtraChunkData(@Nonnull World world, @Nonnull Chunk chunk) {
    }

    @Nullable
    public Chunk loadChunk(@Nonnull World world, int n, int n2) throws IOException {
        Pair<Chunk, NBTTagCompound> pair = this.getValidChunkWithNBT(world, n, n2);
        if (pair != null) {
            Chunk chunk = pair.getKey();
            NBTTagCompound nBTTagCompound = pair.getValue();
            this.loadEntitiesInChunks(world, nBTTagCompound.getCompoundTag("Level"), chunk);
            return chunk;
        }
        return null;
    }

    public void chunkTick() {
    }

    @Nullable
    public static Entity initEntity(NBTTagCompound tagCompound, World world, Chunk chunk) {
        Entity entity = HYTChunkLoader.createEntity(tagCompound, world);
        if (entity == null) {
            return null;
        }
        chunk.addEntity(entity);
        if (tagCompound.hasKey("Passengers", 9)) {
            NBTTagList passengers = tagCompound.getTagList("Passengers", 10);
            int i = 0;
            while (i < passengers.tagCount()) {
                Entity initedEntity = HYTChunkLoader.initEntity(passengers.getCompoundTagAt(i), world, chunk);
                if (initedEntity != null) {
                    initedEntity.startRiding(entity, true);
                }
                ++i;
            }
        }
        return entity;
    }
}

