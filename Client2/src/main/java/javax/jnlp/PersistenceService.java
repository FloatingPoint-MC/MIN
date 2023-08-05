
package javax.jnlp;

public interface PersistenceService {

  int CACHED = 0;
  int TEMPORARY = 1;
  int DIRTY = 2;

  long create(java.net.URL url, long maxsize) throws java.io.IOException;
  FileContents get(java.net.URL url) throws java.io.IOException;
  void delete(java.net.URL url) throws java.io.IOException;
  String[] getNames(java.net.URL url) throws java.io.IOException;
  int getTag(java.net.URL url) throws java.io.IOException;
  void setTag(java.net.URL url, int tag) throws java.io.IOException;

}

