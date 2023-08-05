

package javax.jnlp;

public interface ServiceManagerStub {

  Object lookup(String name) throws UnavailableServiceException;
  String[] getServiceNames();

}

