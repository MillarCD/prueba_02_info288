## Compilar

```
bash compilar.sh
```

## Como compilar manualmente:

**1) compilar archivos**

```
cd server
javac LogRecorder.java
javac LogRecorderImp.java
javac Server.java
javac ConfigEnv.java
```

**1.2) copiar los archivos LogRecorder.class y ConfigEnv.class en la carpeta /client**

```
cd ../client
javac Client.java
javac ConfigEnv.java
cd ..
```

## Crear clientes

Para ejecutar multiples clientes copiar la carpeta client y 
modificar su archivo de configuracion como se explica acontinuacion.

## Archivos de configuracion

**Crear archivo config en la carpeta client con los siguietes campos**

```
SERVER_IP=<IP del servidor>
SERVER_PORT=<Puerto del servidor>
CLIENT_NAME=<Nombre del cliente>
LOG_PATH=<Ruta del log que se va a leer>
TIME_TO_READ=<Tiempo de espera para volver a leer el archivo (en ms)>
CURSOR=<ultima linea leida>
```

**Crear archivo config en la carpeta server con los siguietes campos**

```
PORT=<Puerto que escuchara el servidor>
LOG_PATH=<Ruta al archivo .log>
```


## Ejecucion:

**Abrir 5 consolas**

  **1) En la primera  ejecutar el siguiete comando para habilitar la escucha del puerto 3000 para RMI**
    
    ```
    cd server
    rmiregistry <Puerto asignado en el archivo config del servidor>
    ```

  **2) En la segunda ejecutar el sevidor:**

  ```
  cd server
  java Server
  ```

  **3 y 4) En la tercera y cuarta consola ejecutar los clientes**

  ```
  cd client
  java Client
  ```
