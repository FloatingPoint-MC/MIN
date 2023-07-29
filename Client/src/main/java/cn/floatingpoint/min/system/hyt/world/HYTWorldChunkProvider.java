package cn.floatingpoint.min.system.hyt.world;

import cn.floatingpoint.min.system.hyt.packet.impl.Hyt0Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.IChunkLoader;

import java.io.File;
import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-17 17:09:09
 */
public class HYTWorldChunkProvider extends ChunkProviderClient {
    public static HYTWorldChunkProvider instance;
    private final IChunkLoader chunkLoader;

    public HYTWorldChunkProvider(World worldIn) {
        super(worldIn);
        instance = this;
        chunkLoader = new HYTChunkLoader(new File(Minecraft.getMinecraft().getDataDir(), "MIN/worlds/" + Hyt0Packet.worldName), Minecraft.getMinecraft().getDataFixer());
    }

    @Override
    public Chunk loadChunk(int chunkX, int chunkZ) {
        try {
            Chunk chunk = chunkLoader.loadChunk(Minecraft.getMinecraft().world, chunkX, chunkZ);
            long l = ChunkPos.asLong(chunkX, chunkZ);
            assert chunk != null;
            chunk.setLastSaveTime(Minecraft.getMinecraft().world.getTotalWorldTime());
            chunk.markLoaded(true);
            loadedChunks.put(l, chunk);

            return chunk;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
