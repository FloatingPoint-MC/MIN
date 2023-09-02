package cn.floatingpoint.min.system.hyt.world;

import com.google.common.collect.Maps;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HYTChunkManager {
    private static final Map<File, HYTChunk> chunks = Maps.newHashMap();

    public static boolean hasChunkGenerated(File file, int n, int n2) {
        HYTChunk chunk = loadChunkFile(file, n, n2);
        return chunk != null && chunk.hasChunkData(n & 0x1F, n2 & 0x1F);
    }

    public static DataInputStream getChunkFileInputStream(File file, int n, int n2) throws IOException {
        HYTChunk chunk = loadChunkFile(file, n, n2);
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

    public static synchronized HYTChunk loadChunkFile(File file, int n, int n2) {
        File file2 = new File(file, "region");
        File file3 = new File(file2, "r." + (n >> 5) + "." + (n2 >> 5) + ".mca");
        HYTChunk class781 = chunks.get(file3);
        if (class781 != null) {
            return class781;
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

