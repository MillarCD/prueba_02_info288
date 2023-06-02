echo "Compilar archivos.java"

echo "compilando archivos del servidor"
cd server
javac LogRecorder.java
javac LogRecorderImp.java
javac Server.java
javac ConfigEnv.java

cp LogRecorder.class ../client

echo "compilando archivos del cliente"
cd ../client
javac Client.java
javac ConfigEnv.java
