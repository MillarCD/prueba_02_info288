## Api rest maestro

### Archivos de configuracion

Definir en un archivo esclavos.json las categorias de los esclavos
y las direcciones donde estan corriendo:

```
{
  "computacion": "127.0.0.1:3000"
  "deporte": "127.0.0.1:3001"
}
```


### Como ejecutar

`
  uvicorn app:app --host 0.0.0.0 --port <PORT>
`
