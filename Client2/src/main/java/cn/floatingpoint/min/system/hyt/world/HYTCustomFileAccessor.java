package cn.floatingpoint.min.system.hyt.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HYTCustomFileAccessor extends RandomAccessFile {
    private long crypt = 0L;
    private static final byte[] cryptArray = new byte[256];

    @Override
    public int read() throws IOException {
        int n = super.read();
        if (n != -1) {
            ++this.crypt;
            return n ^ cryptArray[(int) (crypt % (long) cryptArray.length)];
        }
        return n;
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3 = super.read(byArray, n, n2);
        if (n3 != -1) {
            int i = 0;
            while (i < n2) {
                byArray[++i + n] = (byte) (byArray[n3 + n] ^ HYTCustomFileAccessor.cryptArray[(int) (((long) i + crypt) % (long) HYTCustomFileAccessor.cryptArray.length)]);
            }
            crypt += n3;
        }
        return n3;
    }

    @Override
    public void seek(long l) throws IOException {
        super.seek(l);
        this.crypt = l;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        int n = super.read(byArray);
        if (n != -1) {
            int i = 0;
            while (i < byArray.length) {
                byArray[++i] = (byte) (byArray[i] ^ HYTCustomFileAccessor.cryptArray[(int) (((long) i + crypt) % (long) HYTCustomFileAccessor.cryptArray.length)]);
            }
            crypt += n;
        }
        return n;
    }

    public HYTCustomFileAccessor(File file, String string) throws FileNotFoundException {
        super(file, string);
    }

    static {
        cryptArray[0] = 7;
        cryptArray[1] = 56;
        cryptArray[2] = 3;
        cryptArray[3] = 55;
        cryptArray[4] = 108;
        cryptArray[5] = 28;
        cryptArray[6] = 105;
        cryptArray[7] = 105;
        cryptArray[8] = 83;
        cryptArray[9] = 34;
        cryptArray[10] = 22;
        cryptArray[11] = 27;
        cryptArray[12] = 125;
        cryptArray[13] = 23;
        cryptArray[14] = 62;
        cryptArray[15] = 74;
        cryptArray[16] = 22;
        cryptArray[17] = 81;
        cryptArray[18] = 77;
        cryptArray[19] = 44;
        cryptArray[20] = 124;
        cryptArray[21] = 48;
        cryptArray[22] = 94;
        cryptArray[23] = 126;
        cryptArray[24] = 102;
        cryptArray[25] = 39;
        cryptArray[26] = 62;
        cryptArray[27] = 91;
        cryptArray[28] = 33;
        cryptArray[29] = 60;
        cryptArray[30] = 82;
        cryptArray[31] = 44;
        cryptArray[32] = 61;
        cryptArray[33] = 93;
        cryptArray[34] = 124;
        cryptArray[35] = 64;
        cryptArray[36] = 102;
        cryptArray[37] = 68;
        cryptArray[38] = 5;
        cryptArray[39] = 31;
        cryptArray[40] = 7;
        cryptArray[41] = 25;
        cryptArray[42] = 55;
        cryptArray[43] = 66;
        cryptArray[44] = 37;
        cryptArray[45] = 114;
        cryptArray[46] = 10;
        cryptArray[47] = 50;
        cryptArray[48] = 58;
        cryptArray[49] = 115;
        cryptArray[50] = 34;
        cryptArray[51] = 67;
        cryptArray[52] = 36;
        cryptArray[53] = 12;
        cryptArray[54] = 45;
        cryptArray[55] = 95;
        cryptArray[56] = 90;
        cryptArray[57] = 39;
        cryptArray[58] = 19;
        cryptArray[59] = 13;
        cryptArray[60] = 127;
        cryptArray[61] = 16;
        cryptArray[62] = 93;
        cryptArray[63] = 99;
        cryptArray[64] = 79;
        cryptArray[65] = 103;
        cryptArray[66] = 74;
        cryptArray[67] = 66;
        cryptArray[68] = 46;
        cryptArray[69] = 116;
        cryptArray[70] = 81;
        cryptArray[71] = 23;
        cryptArray[72] = 106;
        cryptArray[73] = 96;
        cryptArray[74] = 48;
        cryptArray[75] = 92;
        cryptArray[76] = 100;
        cryptArray[77] = 54;
        cryptArray[78] = 86;
        cryptArray[79] = 3;
        cryptArray[80] = 49;
        cryptArray[81] = 119;
        cryptArray[82] = 126;
        cryptArray[83] = 40;
        cryptArray[84] = 59;
        cryptArray[85] = 103;
        cryptArray[86] = 51;
        cryptArray[87] = 4;
        cryptArray[88] = 71;
        cryptArray[89] = 41;
        cryptArray[90] = 17;
        cryptArray[91] = 56;
        cryptArray[92] = 113;
        cryptArray[93] = 88;
        cryptArray[94] = 79;
        cryptArray[95] = 47;
        cryptArray[96] = 42;
        cryptArray[97] = 125;
        cryptArray[98] = 43;
        cryptArray[99] = 84;
        cryptArray[100] = 113;
        cryptArray[101] = 68;
        cryptArray[102] = 120;
        cryptArray[103] = 52;
        cryptArray[104] = 101;
        cryptArray[105] = 112;
        cryptArray[106] = 127;
        cryptArray[107] = 120;
        cryptArray[108] = 87;
        cryptArray[109] = 95;
        cryptArray[110] = 57;
        cryptArray[111] = 8;
        cryptArray[112] = 30;
        cryptArray[113] = 76;
        cryptArray[114] = 123;
        cryptArray[115] = 38;
        cryptArray[116] = 6;
        cryptArray[117] = 40;
        cryptArray[118] = 24;
        cryptArray[119] = 88;
        cryptArray[120] = 43;
        cryptArray[121] = 14;
        cryptArray[122] = 121;
        cryptArray[123] = 72;
        cryptArray[124] = 57;
        cryptArray[125] = 64;
        cryptArray[126] = 121;
        cryptArray[127] = 122;
        cryptArray[128] = 11;
        cryptArray[129] = 118;
        cryptArray[130] = 78;
        cryptArray[131] = 51;
        cryptArray[132] = 37;
        cryptArray[133] = 65;
        cryptArray[134] = 31;
        cryptArray[135] = 109;
        cryptArray[136] = 114;
        cryptArray[137] = 107;
        cryptArray[138] = 96;
        cryptArray[139] = 36;
        cryptArray[140] = 98;
        cryptArray[141] = 54;
        cryptArray[142] = 106;
        cryptArray[143] = 100;
        cryptArray[144] = 119;
        cryptArray[145] = 59;
        cryptArray[146] = 109;
        cryptArray[147] = 42;
        cryptArray[148] = 75;
        cryptArray[149] = 107;
        cryptArray[150] = 2;
        cryptArray[151] = 11;
        cryptArray[152] = 72;
        cryptArray[153] = 97;
        cryptArray[154] = 14;
        cryptArray[155] = 50;
        cryptArray[156] = 24;
        cryptArray[157] = 122;
        cryptArray[158] = 117;
        cryptArray[159] = 15;
        cryptArray[160] = 97;
        cryptArray[161] = 90;
        cryptArray[162] = 110;
        cryptArray[163] = 19;
        cryptArray[164] = 71;
        cryptArray[165] = 49;
        cryptArray[166] = 33;
        cryptArray[167] = 53;
        cryptArray[168] = 12;
        cryptArray[169] = 20;
        cryptArray[170] = 20;
        cryptArray[171] = 73;
        cryptArray[172] = 26;
        cryptArray[173] = 76;
        cryptArray[174] = 101;
        cryptArray[175] = 104;
        cryptArray[176] = 69;
        cryptArray[177] = 9;
        cryptArray[178] = 111;
        cryptArray[179] = 32;
        cryptArray[180] = 21;
        cryptArray[181] = 4;
        cryptArray[182] = 28;
        cryptArray[183] = 35;
        cryptArray[184] = 91;
        cryptArray[185] = 99;
        cryptArray[186] = 6;
        cryptArray[187] = 1;
        cryptArray[188] = 98;
        cryptArray[189] = 18;
        cryptArray[190] = 1;
        cryptArray[191] = 112;
        cryptArray[192] = 9;
        cryptArray[193] = 8;
        cryptArray[194] = 27;
        cryptArray[195] = 63;
        cryptArray[196] = 89;
        cryptArray[197] = 123;
        cryptArray[198] = 85;
        cryptArray[199] = 69;
        cryptArray[200] = 116;
        cryptArray[201] = 45;
        cryptArray[202] = 70;
        cryptArray[203] = 0;
        cryptArray[204] = 111;
        cryptArray[205] = 17;
        cryptArray[206] = 25;
        cryptArray[207] = 15;
        cryptArray[208] = 46;
        cryptArray[209] = 89;
        cryptArray[210] = 118;
        cryptArray[211] = 77;
        cryptArray[212] = 110;
        cryptArray[213] = 38;
        cryptArray[214] = 78;
        cryptArray[215] = 16;
        cryptArray[216] = 5;
        cryptArray[217] = 61;
        cryptArray[218] = 82;
        cryptArray[219] = 63;
        cryptArray[220] = 80;
        cryptArray[221] = 35;
        cryptArray[222] = 67;
        cryptArray[223] = 52;
        cryptArray[224] = 18;
        cryptArray[225] = 80;
        cryptArray[226] = 13;
        cryptArray[227] = 53;
        cryptArray[228] = 2;
        cryptArray[229] = 127;
        cryptArray[230] = 30;
        cryptArray[231] = 92;
        cryptArray[232] = 87;
        cryptArray[233] = 86;
        cryptArray[234] = 73;
        cryptArray[235] = 115;
        cryptArray[236] = 75;
        cryptArray[237] = 83;
        cryptArray[238] = 26;
        cryptArray[239] = 21;
        cryptArray[240] = 58;
        cryptArray[241] = 85;
        cryptArray[242] = 32;
        cryptArray[243] = 29;
        cryptArray[244] = 70;
        cryptArray[245] = 108;
        cryptArray[246] = 84;
        cryptArray[247] = 104;
        cryptArray[248] = 10;
        cryptArray[249] = 60;
        cryptArray[250] = 29;
        cryptArray[251] = 94;
        cryptArray[252] = 41;
        cryptArray[253] = 47;
        cryptArray[254] = 65;
        cryptArray[255] = 117;
    }
}

