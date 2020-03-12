import paho.mqtt.subscribe as subscribe
import math


#print(msg.payload.decode("utf-8"))
def getsoil_humidity():
    msg = subscribe.simple("soil_humidity", hostname="localhost", port=1883, keepalive=60)
    soil_humidity = float(msg.payload)
    return soil_humidity/10

 