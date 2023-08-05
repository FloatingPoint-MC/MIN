

package javax.jnlp;

public interface PrintService {

  java.awt.print.PageFormat getDefaultPage();
  java.awt.print.PageFormat showPageFormatDialog(java.awt.print.PageFormat page);
  boolean print(java.awt.print.Pageable document);
  boolean print(java.awt.print.Printable painter);

}

