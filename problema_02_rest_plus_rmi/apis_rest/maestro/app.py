from contextlib import asynccontextmanager
from fastapi import FastAPI

import requests

import json

# Diccionario con direcciones de los esclavos
data: dict[str, str] = {}

@asynccontextmanager
async def lifespan(app: FastAPI):
    global data

    with open('./esclavos.json') as f:
        d: dict = json.load(f)

    data = d

    yield


app = FastAPI(lifespan=lifespan)


@app.get("/query")
async def get_products(productos: str | None = None, categorias: str | None = None) -> list[dict]:

    response: list[dict] = []
    if (categorias != None):
        # Busqueda por categorias
        for c in categorias.split():
            address = data.get(c)
            if (address == None): continue

            d = []

            res = requests.get(f'http://{address}/products')
            if (res.status_code == 200):
                d = res.json()

            response += d

    elif (productos != None):
        # Busqueda por productos (Broadcast)

        for c, address in data.items():

            d = []

            res = requests.get(f'http://{address}/products?productos={productos.replace(" ","+")}')
            if (res.status_code == 200):
                d = res.json()

            response += d

    return response
