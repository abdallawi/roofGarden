from datetime import datetime
import time

from influxdb_client import InfluxDBClient, Point
from influxdb_client.client.write_api import SYNCHRONOUS


influx_url = "https://eu-central-1-1.aws.cloud2.influxdata.com"
influx_token = "e9-en5SF1HkWu-uIIOU-L6L42a4TE96UK98qhTxaE3-Chsai9Nq-eiRNAk4WPnxHEMzlAqSD0m_tpd4PZk4BBg=="
organization = "1724611cb04f2363"
bucket = "Test"
client = InfluxDBClient(url=influx_url, token=influx_token)


try:
    kind = "temperature"
    host = "host1"
    device = "opt-123"
    
    while True:
        import tempSub as temperature
        import humiditySub as humidity
        import soil_humiditySub as soil_humidity
        
      
        temperature = temperature.getTemperature()
        humidity = humidity.getHumidity()
        soil_humidity = soil_humidity.getsoil_humidity()
        
        write_api = client.write_api()
    
        #Write data by point structure
        #point = Point(kind).tag('host'.host).tag('device', device).field('value', 35.3).time(time=datetime.utcnow())
        temperaturePoint = Point("temperature").tag("RoofGarden", "temperatuur").field("some_level", temperature)
        print(f'Writing to influxDV cloud: {temperaturePoint.to_line_protocol()}...')
        
        humidityPoint = Point("Humidity").tag("RoofGarden", "humidity").field("some_level", humidity)
        print(f'Writing to influxDV cloud: {humidityPoint.to_line_protocol()}...')
        
        soil_humidityPoint = Point("Soil_humidity").tag("RoofGarden", "soil_humidity").field("some_level", soil_humidity)
        print(f'Writing to influxDV cloud: {soil_humidityPoint.to_line_protocol()}...')
        
        print("temperatur: " )
        print(temperature)
        print("humidity: "   )
        print(humidity)
        print("soil humidity:" )
        print(soil_humidity)
       
        write_api.write(bucket=bucket, org=organization, record=temperaturePoint)
        write_api.write(bucket=bucket, org=organization, record=humidityPoint)
        write_api.write(bucket=bucket, org=organization, record=soil_humidityPoint)
        #client.health()
        time.sleep(6)        
    
    
    print("succes")
    
except Exception as e:
    print(e)
finally:
    client.close()
    print("close ...")
