import paho.mqtt.subscribe as subscribe
import math


#print(msg.payload.decode("utf-8"))
def getTemperature():
    msg = subscribe.simple("temperature", hostname="localhost", port=1883, keepalive=60)
    temperature = float(msg.payload)
    return temperature
