

package javax.jnlp;

public interface FileOpenService {

  FileContents openFileDialog(String pathHint, String[] extensions) throws java.io.IOException;
  FileContents[] openMultiFileDialog(String pathHint, String[] extensions) throws java.io.IOException;

}

