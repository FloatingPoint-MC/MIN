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

    public HYTChunkProviderClient(World world, Properties properties) {
        super(world);
        this.loadedChunks = Sets.newHashSet();
        this.world = world;
        String[] stringArray = new String[2];
        stringArray[0] = "Cache";
        stringArray[1] = properties.getProperty("respath");
        chunkLoader = new HYTChunkLoader(new File(Managers.fileManager.dir, Arrays.stream(stringArray).map(string -> new StringBuilder().insert(0, "/").append(string).toString()).collect(Collectors.joining())), Minecraft.getMinecraft().getDataFixer());
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
    public Chunk loadChunk(int n, int n2) {
        try {
            HYTChunkProviderClient HYTChunkProviderClient = this;
            Chunk chunk = HYTChunkProviderClient.chunkLoader.loadChunk(HYTChunkProviderClient.world, n, n2);
            if (chunk != null) {
                HYTChunkProviderClient class9562 = this;
                chunk.setLastSaveTime(class9562.world.getTotalWorldTime());
                long l = ChunkPos.asLong(n, n2);
                class9562.loadedChunks.add(l);
                chunk.markLoaded(true);
                this.chunkMap.put(l, chunk);
                return chunk;
            }
        }
        catch (Exception exception) {
            logger.error("Couldn't load res chunk", exception);
        }
        return super.loadChunk(n, n2);
    }
}

