import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;

class Client {
  static public void main(String args[]) {
    ConfigEnv configEnv = ConfigEnv.getInstance();


    // Carga variables de entorno
    String clientName = configEnv.getVar("CLIENT_NAME");
    String LOG_PATH = configEnv.getVar("LOG_PATH");
    int cursor = Integer.parseInt(configEnv.getVar("CURSOR"));
    String IP = configEnv.getVar("SERVER_IP");
    int PORT = Integer.parseInt(configEnv.getVar("SERVER_PORT"));
    int TIME_TO_READ = Integer.parseInt(configEnv.getVar("TIME_TO_READ"));

    try {
      String lines = "";
      String log = "";
      long timestamp = 0;
      
      // Server config
      Registry registry = LocateRegistry.getRegistry(IP, PORT);
      LogRecorder lg = (LogRecorder) registry.lookup("Logger");

      System.out.println("[" + clientName + "]: listo para leer logs");

      while(true) {
	System.out.println("Leyendo archivo: " + LOG_PATH);
	lines = readLogFile(LOG_PATH, cursor);

	
	if (lines.length() == 0) {
	  Thread.sleep(TIME_TO_READ);
	  continue;
	}

	System.out.println("" + lines.split("\n").length + " registros nuevas");
	// Enviar logs al servidor
	for (String line : lines.split("\n")) {
	  timestamp = Instant.now().getEpochSecond();
	  
	  log = line + "; " + timestamp;

	  System.out.println("log nuevo: " +  log);
	  lg.saveLog(clientName, log);

	  cursor++;
	  // Actualiza el valor de la ultima linea leida en el archivo config
	  configEnv.setVar("CURSOR", "" + cursor);
	}

	Thread.sleep(TIME_TO_READ);
      }


    } catch (RemoteException e) {
      System.err.println("Error de comunicacio: " + e.toString());
    } catch (Exception e) {
      System.err.println("Excepcion en logger: ");
      e.printStackTrace();
    } finally {
      configEnv.setVar("CURSOR", "" + cursor);
    }
  }

  /**
   * Funcion que retorna las lineas que no se han registrado
   * en el log central
   */
  static private String readLogFile(String path, int cursor) {
    String newLines = "";
    try {
      FileReader file = new FileReader(path);
      BufferedReader br = new BufferedReader(file);

      String line;

      int counter = 1;
      while ((line = br.readLine()) != null) {
	if (counter > cursor) newLines += line + "\n";
	counter++;
      }

      br.close();
      file.close();

    } catch (Exception e) {
      System.err.println("Error en la lectura del archivo");
    }

    return newLines;
  }
}
  
