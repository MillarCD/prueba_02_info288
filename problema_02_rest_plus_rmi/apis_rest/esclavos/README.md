# Aplicacion esclavo

## Archivos de configuracion necesarios

Crear base de datos: la cual consiste en un archivo .csv con los campos
category, product y price

Crear archivo .env con los siguientes campos

```
CATEGORY=<Nombre de la categoria>
DB_PATH=<ruta a la base de datos>
LOG_PATH=<ruta donde se almacenara el log>
```

## Como Ejecutar:

```
  uvicorn app:app --host 0.0.0.0 --port <PORT>
```
