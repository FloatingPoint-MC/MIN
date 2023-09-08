package cn.floatingpoint.min.system.hyt.world;

import cn.floatingpoint.min.management.Managers;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HYTChunkManager {
    private static final Map<File, HYTChunk> chunks = Maps.newHashMap();

    public static boolean hasChunkGenerated(String worldName, int n, int n2) {
        HYTChunk chunk = loadChunkFile(worldName, n, n2);
        return chunk != null && chunk.hasChunkData(n & 0x1F, n2 & 0x1F);
    }

    public static DataInputStream getChunkFileInputStream(String worldName, int n, int n2) throws IOException {
        HYTChunk chunk = loadChunkFile(worldName, n, n2);
        if (chunk == null) {
            return null;
        }
        return chunk.getDataInputStream(n & 0x1F, n2 & 0x1F);
    }

    public static synchronized void exitChunkExecutor() {
        for (HYTChunk chunk : chunks.values()) {
            try {
                if (chunk == null) continue;
                chunk.closeStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        chunks.clear();
    }

    public static synchronized HYTChunk loadChunkFile(String worldName, int n, int n2) {
        File file = new File(Managers.fileManager.dir, "hyt/saves/" + worldName);
        File file2 = new File(file, "region");
        String rcaFormat = "r." + (n >> 5) + "." + (n2 >> 5) + ".mca";
        File file3 = new File(file2, rcaFormat);
        if (!file3.exists() || (!file3.isFile() && file3.delete())) {
            Managers.fileManager.extractFile(new ResourceLocation("min/hyt/saves/" + worldName + "/region/" + rcaFormat), file3);
        }
        HYTChunk hytChunk = chunks.get(file3);
        if (hytChunk != null) {
            return hytChunk;
        }
        if (file2.exists() && file3.exists()) {
            if (chunks.size() >= 256) {
                exitChunkExecutor();
            }
            HYTChunk chunk = new HYTChunk(file3);
            chunks.put(file3, chunk);
            return chunk;
        }
        return null;
    }
}

