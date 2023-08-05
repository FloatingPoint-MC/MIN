

package javax.jnlp;

public interface ClipboardService {

  java.awt.datatransfer.Transferable getContents();
  void setContents(java.awt.datatransfer.Transferable contents);

}

