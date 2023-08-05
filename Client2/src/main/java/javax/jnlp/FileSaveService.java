

package javax.jnlp;

public interface FileSaveService {

  FileContents saveFileDialog(String pathHint, String[] extensions, java.io.InputStream stream, String name) throws java.io.IOException;
  FileContents saveAsFileDialog(String pathHint, String[] extensions, FileContents contents) throws java.io.IOException;

}

