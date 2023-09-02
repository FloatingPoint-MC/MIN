package cn.floatingpoint.min.system.hyt.world;

import com.google.common.collect.Lists;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.annotation.Nullable;

public class HYTChunk {
    private List<Boolean> booleanList;
    private RandomAccessFile accessFile;
    private final int[] chunkData;

    @Deprecated
    public synchronized boolean Method3254(int n, int n2) {
        return this.hasChunkData(n, n2);
    }

    public HYTChunk(File file) {
        this.chunkData = new int[1024];
        try {
            int data;
            this.accessFile = new HYTCustomFileAccessor(file, "r");
            int length = (int) this.accessFile.length() / 4096;
            this.booleanList = Lists.newArrayListWithCapacity(length);
            int i = 0;
            while (i < length) {
                this.booleanList.add(true);
                i++;
            }
            this.booleanList.set(0, false);
            this.booleanList.set(1, false);
            this.accessFile.seek(0L);
            i = 0;
            while (i < 1024) {
                chunkData[i] = data = accessFile.readInt();
                int dataLength = data & 0xFF;
                if (dataLength == 255 && data >> 8 <= this.booleanList.size()) {
                    accessFile.seek((data >> 8) * 4096L);
                    dataLength = (accessFile.readInt() + 4) / 4096 + 1;
                    accessFile.seek(i * 4 + 4);
                }
                if (data != 0 && (data >> 8) + dataLength <= this.booleanList.size()) {
                    int i1 = 0;
                    while (i1 < dataLength) {
                        int chunkPosition = (data >> 8) + i1;
                        this.booleanList.set(chunkPosition, false);
                        ++i1;
                    }
                }
                i++;
            }
            i = 0;
            while (i < 1024) {
                accessFile.readInt();
                i++;
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void closeStream() throws IOException {
        if (this.accessFile != null) {
            this.accessFile.close();
        }
    }

    @Nullable
    public synchronized DataInputStream getDataInputStream(int x, int z) throws IOException {
        int n3;
        if (this.isChunkInvalid(x, z)) {
            return null;
        }
        n3 = this.getChunkData(x, z);
        if (n3 == 0) {
            return null;
        }
        int n4 = n3 >> 8;
        int n5 = n3 & 0xFF;
        if (n5 == 255) {
            accessFile.seek(n4 * 4096L);
            n5 = (accessFile.readInt() + 4) / 4096 + 1;
        }
        if (n4 + n5 > this.booleanList.size()) {
            return null;
        }
        accessFile.seek(n4 * 4096L);
        int n6 = accessFile.readInt();
        if (n6 > 4096 * n5) {
            return null;
        }
        if (n6 <= 0) {
            return null;
        }
        byte by = this.accessFile.readByte();
        if (by == 1) {
            byte[] byArray = new byte[n6 - 1];
            this.accessFile.read(byArray);
            return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(byArray))));
        }
        if (by == 2) {
            byte[] byArray = new byte[n6 - 1];
            this.accessFile.read(byArray);
            return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(byArray))));
        }
        return null;
    }

    private int getChunkData(int x, int z) {
        return this.chunkData[x + z * 32];
    }

    private boolean isChunkInvalid(int x, int z) {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    public boolean hasChunkData(int x, int z) {
        return this.getChunkData(x, z) != 0;
    }
}

