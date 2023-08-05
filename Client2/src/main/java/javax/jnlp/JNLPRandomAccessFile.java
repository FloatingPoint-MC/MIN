

package javax.jnlp;

public interface JNLPRandomAccessFile {

  void close() throws java.io.IOException;
  long length() throws java.io.IOException;
  long getFilePointer() throws java.io.IOException;
  int read() throws java.io.IOException;
  int read(byte[] b, int off, int len) throws java.io.IOException;
  int read(byte[] b) throws java.io.IOException;
  void readFully(byte[] b) throws java.io.IOException;
  void readFully(byte[] b, int off, int len) throws java.io.IOException;
  int skipBytes(int n) throws java.io.IOException;
  boolean readBoolean() throws java.io.IOException;
  byte readByte() throws java.io.IOException;
  int readUnsignedByte() throws java.io.IOException;
  short readShort() throws java.io.IOException;
  int readUnsignedShort() throws java.io.IOException;
  char readChar() throws java.io.IOException;
  int readInt() throws java.io.IOException;
  long readLong() throws java.io.IOException;
  float readFloat() throws java.io.IOException;
  double readDouble() throws java.io.IOException;
  String readLine() throws java.io.IOException;
  String readUTF() throws java.io.IOException;
  void seek(long pos) throws java.io.IOException;
  void setLength(long newLength) throws java.io.IOException;
  void write(int b) throws java.io.IOException;
  void write(byte[] b) throws java.io.IOException;
  void write(byte[] b, int off, int len) throws java.io.IOException;
  void writeBoolean(boolean v) throws java.io.IOException;
  void writeByte(int v) throws java.io.IOException;
  void writeShort(int v) throws java.io.IOException;
  void writeChar(int v) throws java.io.IOException;
  void writeInt(int v) throws java.io.IOException;
  void writeLong(long v) throws java.io.IOException;
  void writeFloat(float v) throws java.io.IOException;
  void writeDouble(double v) throws java.io.IOException;
  void writeBytes(String s) throws java.io.IOException;
  void writeChars(String s) throws java.io.IOException;
  void writeUTF(String str) throws java.io.IOException;

}

