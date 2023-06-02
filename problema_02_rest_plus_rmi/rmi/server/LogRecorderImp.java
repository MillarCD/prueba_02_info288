import java.io.BufferedWriter;
import java.io.FileWriter;
import java.rmi.*;
import java.rmi.server.*;
import java.time.Instant;

class LogRecorderImp implements LogRecorder {
  LogRecorderImp (String path) throws RemoteException {
    System.out.println("Se inicializo la clase LogRecorderImp");
    PATH = path;
  }
  final String PATH;

  public void saveLog(String clientName, String info) throws RemoteException {
    System.out.println("[MSG] clientName: " + clientName + " info: " + info);

    long time = Instant.now().getEpochSecond();

    try {
      FileWriter file = new FileWriter(PATH, true);
      BufferedWriter bw = new BufferedWriter(file);
      bw.append(info + "; " + clientName + "; " + time + "\n");

      bw.close();
      file.close();
    } catch (Exception e) {}
  }

}
