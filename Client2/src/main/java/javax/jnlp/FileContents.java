

package javax.jnlp;

public interface FileContents {

  String getName() throws java.io.IOException;
  java.io.InputStream getInputStream() throws java.io.IOException;
  java.io.OutputStream getOutputStream(boolean overwrite) throws java.io.IOException;
  long getLength() throws java.io.IOException;
  boolean canRead() throws java.io.IOException;
  boolean canWrite() throws java.io.IOException;
  JNLPRandomAccessFile getRandomAccessFile(String mode) throws java.io.IOException;
  long getMaxLength() throws java.io.IOException;
  long setMaxLength(long maxlength) throws java.io.IOException;

}

