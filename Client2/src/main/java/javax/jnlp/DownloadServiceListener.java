

package javax.jnlp;

public interface DownloadServiceListener {

  void progress(java.net.URL url, String version, long readSoFar, long total, int overallPercent);
  void validating(java.net.URL url, String version, long entry, long total, int overallPercent);
  void upgradingArchive(java.net.URL url, String version, int patchPercent, int overallPercent);
  void downloadFailed(java.net.URL url, String version);

}

