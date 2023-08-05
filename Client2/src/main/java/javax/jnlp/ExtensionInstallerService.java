

package javax.jnlp;

public interface ExtensionInstallerService {

  String getInstallPath();
  String getExtensionVersion();
  java.net.URL getExtensionLocation();
  void hideProgressBar();
  void hideStatusWindow();
  void setHeading(String heading);
  void setStatus(String status);
  void updateProgress(int value);
  void installSucceeded(boolean needsReboot);
  void installFailed();
  void setJREInfo(String platformVersion, String jrePath);
  void setNativeLibraryInfo(String path);
  String getInstalledJRE(java.net.URL url, String version);

}

