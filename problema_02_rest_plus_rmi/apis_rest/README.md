## Instalar librerias

```
  pip install -r requirements.txt
```

## Como ejecutar:

* Abrir por lo menos 4 terminales, una para el maestro y las demas
  para los esclavos

  En las primeras 3 terminales copiar la carpeta esclavos y seguir las
  instrucciones que se indican en el README.md

  En la cuarta terminal ir a la carpeta maestro y modificar seguir los pasos
  de configuracion que se explican en el archivo README.md

* Ejecucion

  En cada una de las terminales ejecutar el siguiente comando

```
  uvicorn app:app --host 0.0.0.0 --port <PORT>
```

## Endpoints:

```
http://localhost:8000/query?categorias=<categorias>
http://localhost:8000/query?productos=<productos>
```
