

package javax.jnlp;

public interface BasicService {

  java.net.URL getCodeBase();
  boolean isOffline();
  boolean showDocument(java.net.URL url);
  boolean isWebBrowserSupported();

}

