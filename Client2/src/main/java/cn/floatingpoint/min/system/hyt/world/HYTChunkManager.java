package cn.floatingpoint.min.system.hyt.world;

import cn.floatingpoint.min.management.Managers;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.storage.RegionFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HYTChunkManager {
    private static final Map<File, RegionFile> chunks = Maps.newHashMap();

    public static boolean hasChunkGenerated(String worldName, int n, int n2) {
        RegionFile chunk = loadChunkFile(worldName, n, n2);
        return chunk != null && chunk.isChunkSaved(n & 0x1F, n2 & 0x1F);
    }

    public static DataInputStream getChunkFileInputStream(String worldName, int x, int z) {
        RegionFile chunk = loadChunkFile(worldName, x, z);
        if (chunk == null) {
            return null;
        }
        return chunk.getChunkDataInputStream(x & 0x1F, z & 0x1F);
    }

    public static synchronized void exitChunkExecutor() {
        for (RegionFile chunk : chunks.values()) {
            try {
                if (chunk == null) continue;
                chunk.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        chunks.clear();
    }

    public static synchronized RegionFile loadChunkFile(String worldName, int n, int n2) {
        File saves = new File(Managers.fileManager.dir, "hyt/saves/" + worldName);
        File region = new File(saves, "region");
        if (!region.exists()) {
            if (region.mkdirs()) {
                return null;
            }
        }
        String rcaFormat = "r." + (n >> 5) + "." + (n2 >> 5) + ".mca";
        File rcaFile = new File(region, rcaFormat);
        if (!rcaFile.exists() || (!rcaFile.isFile() && rcaFile.delete())) {
            Managers.fileManager.extractFile(new ResourceLocation("min/hyt/saves/" + worldName + "/region/" + rcaFormat), rcaFile);
        }
        RegionFile hytChunk = chunks.get(rcaFile);
        if (hytChunk != null) {
            return hytChunk;
        }
        if (region.exists() && rcaFile.exists()) {
            if (chunks.size() >= 256) {
                exitChunkExecutor();
            }
            RegionFile chunk = new RegionFile(rcaFile);
            chunks.put(rcaFile, chunk);
            return chunk;
        }
        return null;
    }
}

