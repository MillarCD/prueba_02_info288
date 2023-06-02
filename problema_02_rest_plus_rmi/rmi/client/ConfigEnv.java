import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class ConfigEnv {
  
  private static ConfigEnv instance;
  private ConfigEnv() {
    map = new HashMap<String, String>();
    readFile();
  }
  Map<String, String> map;
  static final String FILE = "./config";
  
  public static ConfigEnv getInstance() {
    if (instance == null) {
      instance = new ConfigEnv();
    }
    return instance;
  }

  public String getVar(String key) {
    return map.get(key);
  }

  public void setVar(String key, String value) {
    map.put(key, value);

    writeFile();
  }

  private void readFile() {
    String key, value;
    
    try {
      FileReader file = new FileReader(FILE);
      BufferedReader br = new BufferedReader(file);

      String line;
      while ((line = br.readLine()) != null) {
	key = line.split("=")[0];
	value = line.split("=")[1];

	map.put(key, value);
      }

      br.close();
      file.close();
    } catch (Exception e) {}
  }
  private void writeFile() {
    try {
      FileWriter file = new FileWriter(FILE, false);
      BufferedWriter bw = new BufferedWriter(file);

      for (String key : map.keySet()) {
	bw.append(key + "=" + map.get(key) + "\n");
      }

      bw.close();
      file.close();
    } catch (Exception e) {}
  }
}
