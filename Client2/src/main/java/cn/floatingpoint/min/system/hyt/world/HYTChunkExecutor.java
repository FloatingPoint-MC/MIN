package cn.floatingpoint.min.system.hyt.world;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;

import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-09-02 18:56:30
 */
public class HYTChunkExecutor {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static volatile String worldName;
    private static HYTChunkProviderClient chunkLoader;
    private static boolean loaded;

    public static void injectWorldProperties() {
        if (!loaded) {
            chunkLoader = new HYTChunkProviderClient(mc.world, worldName);
            mc.world.setClientChunkProvider(chunkLoader);
            loaded = true;
        }
    }

    public static void markUpdated(int x, int z) {
        mc.world.doPreChunk(x, z, true);
        if (chunkLoader.isChunkLoaded(x, z)) {
            mc.world.markBlockRangeForRenderUpdate(x << 4, 0, z << 4, (x << 4) + 15, 256, (z << 4) + 15);
        }
    }

    public static void reset() {
        chunkLoader = null;
        loaded = false;
        HYTChunkManager.exitChunkExecutor();
    }

    public static void tick() {
        if (loaded) {
            Entity entity;
            if ((entity = mc.getRenderViewEntity()) != null) {
                if (entity.world instanceof WorldClient) {
                    if (entity.ticksExisted % 40 == 0) {
                        HashSet<Long> notLoad = Sets.newHashSet();
                        int renderDistance = mc.gameSettings.renderDistanceChunks;
                        double x = entity.posX;
                        double z = entity.posZ;
                        int flooredX = MathHelper.floor(x / 16.0D);
                        int flooredZ = MathHelper.floor(z / 16.0D);
                        int tempX;
                        for (int chunkPosition = tempX = -renderDistance; chunkPosition <= renderDistance; chunkPosition = tempX) {
                            int tempZ;
                            for (chunkPosition = tempZ = -renderDistance; chunkPosition <= renderDistance; chunkPosition = tempZ) {
                                int var13 = tempX + flooredX;
                                int var14 = tempZ + flooredZ;
                                notLoad.add(ChunkPos.asLong(var13, var14));
                                if (!chunkLoader.isChunkLoaded(var13, var14)) {
                                    markUpdated(var13, var14);
                                }
                                ++tempZ;
                            }
                            ++tempX;
                        }
                        HashSet<Long> chunksToLoad = new HashSet<>(chunkLoader.getLoadedChunks());
                        chunksToLoad.removeAll(notLoad);
                        for (Long seed : chunksToLoad) {
                            Chunk chunk = chunkLoader.getChunk(seed);
                            mc.world.doPreChunk(chunk.x, chunk.z, false);
                        }
                    }
                }
            }
        }
    }
}
