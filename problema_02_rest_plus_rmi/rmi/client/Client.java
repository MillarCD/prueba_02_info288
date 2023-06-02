// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.io.BufferedReader;
import java.io.FileReader;
// import java.rmi.RemoteException;

class Client {
  static public void main(String args[]) {
    /*
     * Config file
     * 	- direccion del archivo .log
     * 	- nombre del archivo
     * 	- tiempo de lectura en milisegundos
     * 	- ip del servidor
     * 	- puerto del servidor
     * 	- cursor
     */
    ConfigEnv configEnv = ConfigEnv.getInstance();

    String clientName = configEnv.getVar("CLIENT_NAME");

    String filename = configEnv.getVar("LOG_FILE");
    String LOG_PATH = configEnv.getVar("LOG_PATH");
    int cursor = Integer.parseInt(configEnv.getVar("CURSOR"));

    String IP = configEnv.getVar("SERVER_IP");
    int PORT = Integer.parseInt(configEnv.getVar("SERVER_PORT"));
    int TIME_TO_READ = Integer.parseInt(configEnv.getVar("TIME_TO_READ"));

    try {
      String lines = "";
      long timestamp = 0;

      while(true) {
//	lg.saveLog(clientName, log);

	/* Flujo de trabajo
	 * + leemos el log, desde la ultima linea
	 * + retornamos las listas de lineas
	 * + recalculamos el valor de cursor
	 * + calcularmos los tiempos
	 * enviamos la informacion al servidor mas los datos del cliente
	 */

	lines = readLogFile(filename, LOG_PATH, cursor);

	System.out.println("Line length: " + lines.length());
	
	if (lines.length() == 0) {
	  Thread.sleep(TIME_TO_READ);
	  continue;
	}

	for (String s : lines.split("\n")) {
	  // TODO: enviar info al servidor
	  System.out.println("Linea nueva: " +  s);
	  timestamp = Instant.now().getEpochSecond();
	  System.out.println("time: " + timestamp);

	  cursor++;
	}

	configEnv.setVar("CURSOR", "" + cursor);
	Thread.sleep(TIME_TO_READ);
      }


    // } catch (RemoteException e) {
      // System.err.println("Error de comunicacio: " + e.toString());
    } catch (Exception e) {
      System.err.println("Excepcion en logger: ");
      e.printStackTrace();
    }
  }

  /**
   * Funcion que retorna las lineas que no se han registrado
   * en el log central
   */
  static private String readLogFile(String filename, String path, int cursor) {
    String newLines = "";
    System.out.println("filename: " + filename + " path: " + path);
    try {
      FileReader file = new FileReader(path + "/" + filename);
      BufferedReader br = new BufferedReader(file);

      String line;

      int counter = 1;
      while ((line = br.readLine()) != null) {
	if (counter < cursor) continue;


	newLines += line + "\n";
      }

      br.close();
      file.close();

    } catch (Exception e) {
      System.err.println("Error en la lectura del archivo");
    }

    return newLines;
  }
}
  
