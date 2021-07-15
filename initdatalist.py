import requests
import json
import time
from configparser import ConfigParser
import pymongo
#1件取得
def find_one(collection):
    return collection.find_one()

#全件取得
def find_all(collection):
    for item in collection.find():
        print(item)

#挿入
def insert(col,doc):
    col.insert(doc)

#削除
def delete(col,doc):
    col.remove(doc)

#JSONRPCリクエスト
def jsonrpc():
    config = ConfigParser()
    config.read('config.ini')
    firstblock = int(config['jsonrpc']['firstblock'])
    lastblock = int(config['jsonrpc']['lastblock'])

    #init/dataにデータのあるトランザクション(key:txhash,value:init/data)
    areadata = {}

    block = firstblock

    while (block > lastblock):
        print("now block:"+str(block))
        #RPCリクエストヘッダー指定
        headers = {"content-type": "application/json"}
        payload = {"jsonrpc": "2.0","method":"eth_getBlockByNumber","params":[hex(block),True],"id":1}

        #RPCリクエスト送信
        r = requests.post("http://" + config['geth']['host'] + ":" + config['geth']['port'] + "/",headers=headers,json=payload)
        #レスポンスjsonを変数に格納し、rを解放
        jsondata = r.json()
        if jsondata['result'] is not None:
            i = 0
            for i in range(len(jsondata['result']['transactions'])):
                transaction = jsondata['result']['transactions'][i]
                if transaction['input']:
                    areadata[transaction['hash']] = transaction['input']
        block = block - 1
        time.sleep(10)
    del r
    return areadata

def main():
    config = ConfigParser()
    config.read('config.ini')
    #JSONRPC実行
    pd = jsonrpc()
    print(pd)

    pymon = pymongo.MongoClient()
    db = pymon[config['database']['dbname']]
    col = db[config['database']['collectionname']]
    #取得した値をDBに格納
    for key,value in pd.items():
        col.insert({"txhash" : key,"initdata":value})

    #ファイル出力
    with open("initdata.txt", 'wt') as f:
        for j in range(len(initdata)):
            f.write(str(j)+':'+initdata[j]+'\n')
    f.close()


if __name__ == '__main__':
    main()
