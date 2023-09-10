package cn.floatingpoint.min.system.hyt.world;

import cn.floatingpoint.min.management.Managers;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class HYTChunkProviderClient
extends ChunkProviderClient {
    private final Set<Long> loadedChunks;
    private static final Logger logger = LogManager.getLogger();
    private final HYTChunkLoader chunkLoader;
    private final Long2ObjectMap<Chunk> chunkMap;
    private final World world;

    public Chunk getChunk(long seed) {
        return this.chunkMap.get(seed);
    }

    public HYTChunkProviderClient(World world, String worldName) {
        super(world);
        this.loadedChunks = Sets.newHashSet();
        this.world = world;
        this.chunkLoader = new HYTChunkLoader(worldName, Minecraft.getMinecraft().getDataFixer());
        this.chunkMap = super.loadedChunks;
    }

    public void unloadChunk(int n, int n2) {
        super.unloadChunk(n, n2);
        loadedChunks.remove(ChunkPos.asLong(n, n2));
    }

    public Set<Long> getLoadedChunks() {
        return this.loadedChunks;
    }

    public boolean isChunkLoaded(int x, int z) {
        return this.loadedChunks.contains(ChunkPos.asLong(x, z));
    }

    @Nonnull
    public Chunk loadChunk(int x, int z) {
        try {
            HYTChunkProviderClient HYTChunkProviderClient = this;
            Chunk chunk = HYTChunkProviderClient.chunkLoader.loadChunk(HYTChunkProviderClient.world, x, z);
            if (chunk != null) {
                chunk.setLastSaveTime(world.getTotalWorldTime());
                long seed = ChunkPos.asLong(x, z);
                loadedChunks.add(seed);
                chunk.markLoaded(true);
                this.chunkMap.put(seed, chunk);
                return chunk;
            }
        }
        catch (Exception exception) {
            logger.error("Couldn't load res chunk", exception);
        }
        return super.loadChunk(x, z);
    }
}

