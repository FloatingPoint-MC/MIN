

package javax.jnlp;

public interface DownloadService {

  boolean isResourceCached(java.net.URL ref, String version);
  boolean isPartCached(String part);
  boolean isPartCached(String[] parts);
  boolean isExtensionPartCached(java.net.URL ref, String version, String part);
  boolean isExtensionPartCached(java.net.URL ref, String version, String[] parts);
  void loadResource(java.net.URL ref, String version, DownloadServiceListener progress) throws java.io.IOException;
  void loadPart(String part, DownloadServiceListener progress) throws java.io.IOException;
  void loadPart(String[] parts, DownloadServiceListener progress) throws java.io.IOException;
  void loadExtensionPart(java.net.URL ref, String version, String part, DownloadServiceListener progress) throws java.io.IOException;
  void loadExtensionPart(java.net.URL ref, String version, String[] parts, DownloadServiceListener progress) throws java.io.IOException;
  void removeResource(java.net.URL ref, String version) throws java.io.IOException;
  void removePart(String part) throws java.io.IOException;
  void removePart(String[] parts) throws java.io.IOException;
  void removeExtensionPart(java.net.URL ref, String version, String part) throws java.io.IOException;
  void removeExtensionPart(java.net.URL ref, String version, String[] parts) throws java.io.IOException;
  DownloadServiceListener getDefaultProgressWindow();

}

