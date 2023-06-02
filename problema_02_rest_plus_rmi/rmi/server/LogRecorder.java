import java.rmi.*;

public interface LogRecorder extends Remote {
  void saveLog(String clientName, String info) throws RemoteException;
}
