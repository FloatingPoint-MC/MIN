package cn.floatingpoint.min.system.hyt.party;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class ButtonDecoder {
    private final String[] elements;

    public ButtonDecoder(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        elements = decode(bytes).split("<&>");
    }

    private String decode(byte[] bytes) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] array = new byte[256];
            int read;
            while ((read = gZIPInputStream.read(array)) >= 0) {
                byteArrayOutputStream.write(array, 0, read);
            }
            return byteArrayOutputStream.toString("UTF-8");
        } catch (IOException ignored) {
        }
        return "";
    }

    public boolean containsButtons(String... buttons) {
        for (String button : buttons) {
            if (!this.containsButton(button)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsButton(String btn) {
        for (String element : elements) {
            if (element.endsWith("[but]" + btn)) {
                return true;
            }
        }
        return false;
    }

    public VexViewButton getButton(String name) {
        for (int i = 0; i < this.elements.length; ++i) {
            String e = this.elements[i];
            if (e.endsWith("[but]" + name)) {
                return new VexViewButton(name, this.elements[i + 6]);
            }
        }
        return null;
    }

    public int getButtonIndex(String name) {
        for (int i = 0; i < this.elements.length; ++i) {
            String e = this.elements[i];
            if (e.endsWith("[but]" + name)) {
                return i;
            }
        }
        return 0;
    }

    public String getElement(int index) {
        return elements[index];
    }
}
