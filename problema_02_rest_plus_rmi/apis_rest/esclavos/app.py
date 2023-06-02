from contextlib import asynccontextmanager
from fastapi import FastAPI
from time import time, sleep

from dotenv import load_dotenv
import json
import pandas as pd

import logging
import os


category = ""
df: pd.DataFrame = None
logger: logging.Logger = None

@asynccontextmanager
async def lifespan(app: FastAPI):
    global category, df, logger

    load_dotenv()

    category = os.getenv("CATEGORY")
    DB_PATH = os.getenv("DB_PATH")
    LOG_PATH = os.getenv("LOG_PATH")
    
    # Extrae los datos del archivo .csv
    df = pd.read_csv(DB_PATH)

    ## logging config
    logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(message)s')
    logger = logging.getLogger()

    file_handler = logging.FileHandler(f"{LOG_PATH}")
    logger.addHandler(file_handler)

    file_formatter = logging.Formatter('%(message)s')
    file_handler.setFormatter(file_formatter)
    yield

app = FastAPI(lifespan=lifespan)

def write_log(msg: str) -> None:
    """
        Funcion encargada de escribir en el archivo .log
    """
    logger.info(f"{int(time())}; {msg}")


@app.get("/products")
async def get_products(productos: str | None = None) -> list[dict]:
    """
       Devuelve una lista de productos con los campos: category, product y price 
    """
    if (productos == None):
        msg = "buscar por categoria"
    else:
        msg = "buscar por producto"
    write_log(f"{msg}; ini")

    res: str | None = ''
    if (productos == None):
        res = df.to_json(orient="records")
    else:
        res = df.loc[df['product'].isin(productos.split())].to_json(orient="records")

    sleep(1)

    write_log(f"{msg}; fin")
    return json.loads(res if (res != None) else "[]")
