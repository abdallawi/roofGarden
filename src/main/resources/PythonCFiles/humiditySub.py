import paho.mqtt.subscribe as subscribe
import math


#print(msg.payload.decode("utf-8"))
def getHumidity():
    msg = subscribe.simple("humidity", hostname="localhost", port=1883, keepalive=60)
    humidity = float(msg.payload)
    return humidity
