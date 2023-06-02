import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

class Server {
  static public void main(String args[]) {
    System.setProperty("java.rmi.server.hostname", "127.0.0.1");

    ConfigEnv configEnv = ConfigEnv.getInstance();
    
    int PORT = Integer.parseInt(configEnv.getVar("PORT"));
    String logPath = configEnv.getVar("LOG_PATH");

    try {
      LogRecorderImp logImp = new LogRecorderImp(logPath);
      LogRecorder stub = (LogRecorder) UnicastRemoteObject.exportObject(logImp, 0);

      Registry registry = LocateRegistry.getRegistry("127.0.0.1",PORT);
      System.out.println("Servidor escuchando en el puerto " + String.valueOf(PORT));
      registry.bind("Logger", stub);

    } catch (RemoteException e) {
      System.err.println("Comunication Error: " + e.toString());
      System.exit(1);
    } catch (Exception e) {
      System.err.println("Exception");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
