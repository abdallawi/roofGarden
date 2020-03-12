/***************************************************
  Adafruit MQTT Library ESP8266 Example
  Must use ESP8266 Arduino from:
    https://github.com/esp8266/Arduino
  Works great with Adafruit's Huzzah ESP board & Feather
  ----> https://www.adafruit.com/product/2471
  ----> https://www.adafruit.com/products/2821
  Adafruit invests time and resources providing this open source code,
  please support Adafruit and open-source hardware by purchasing
  products from Adafruit!
  Written by Tony DiCola for Adafruit Industries.
  MIT license, all text above must be included in any redistribution
 ****************************************************/
#include <ESP8266WiFi.h>
#include "Adafruit_MQTT.h"
#include "Adafruit_MQTT_Client.h"
#include "DHT.h"

#define DHTPIN 2

#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);


float temperature=0;
float humidity=0;

int moisture_Pin= 0;
float soil_moisture;
/************************* WiFi Access Point *********************************/

#define WLAN_SSID       "StartProjecten.FabLab" // enter your WiFi SSID
#define WLAN_PASS       "ConnectSP3336666" // this is your WiFi password

/************************* Adafruit.io Setup *********************************/

#define MQTT_SERVER      "192.168.68.117" // change this to your Pi IP_address
#define MQTT_SERVERPORT  1883                   // use 8883 for SSL
#define MQTT_USERNAME    "" // empty
#define MQTT_KEY         "" // empty

/************ Global State (you don't need to change this!) ******************/

// Create an ESP8266 WiFiClient class to connect to the MQTT server.
WiFiClient client;
// or... use WiFiFlientSecure for SSL
//WiFiClientSecure client;

// Setup the MQTT client class by passing in the WiFi client and MQTT server and login details.
Adafruit_MQTT_Client mqtt(&client, MQTT_SERVER, MQTT_SERVERPORT, MQTT_USERNAME, MQTT_KEY);

/****************************** data stream publish/subscribe ***************************************/

Adafruit_MQTT_Publish temperature_stream = Adafruit_MQTT_Publish(&mqtt, MQTT_USERNAME "temperature"); 
Adafruit_MQTT_Publish humidity_stream = Adafruit_MQTT_Publish(&mqtt, MQTT_USERNAME "humidity"); 
Adafruit_MQTT_Publish soil_humidity_stream = Adafruit_MQTT_Publish(&mqtt, MQTT_USERNAME "soil_humidity"); 
//Adafruit_MQTT_Publish pi_led = Adafruit_MQTT_Publish(&mqtt, MQTT_USERNAME "/pi/led"); // ignore this for now


/*************************** Sketch Code ************************************/

// Bug workaround for Arduino 1.6.6, it seems to need a function declaration
// for some reason (only affects ESP8266, likely an arduino-builder bug).
//void MQTT_connect();

void setup() {
  Serial.begin(115200);
  delay(10);

  // Connect to WiFi access point.
  Serial.println(); Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WLAN_SSID);

  WiFi.begin(WLAN_SSID, WLAN_PASS);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.println("WiFi connected");
  Serial.println("IP address: "); 
  Serial.println(WiFi.localIP());

  dht.begin();
}

uint32_t x=0;

void loop() {
  // Ensure the connection to the MQTT server is alive (this will make the first
  // connection and automatically reconnect when disconnected).  See the MQTT_connect
  // function definition further below.
  
  //MQTT_connect();
  mqtt.connect();

  // Read humidity (%)
  humidity = dht.readHumidity();
  // Read temperature as Celsius (the default)
  temperature = dht.readTemperature();
  // read soil moisture from pin A0
  soil_moisture = analogRead(moisture_Pin);
  if (isnan(humidity) || isnan(temperature)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }
  Serial.print("temperature read: ");
  Serial.println(dht.readTemperature());  
  Serial.print("humidity read: ");
  Serial.println(dht.readHumidity());
  Serial.print("Soil moisture read: ");
  Serial.println(soil_moisture); 
  temperature_stream.publish(dht.readTemperature()); // publish to Raspberry Pi under topic "temperature"
  humidity_stream.publish(dht.readHumidity()); // publish to Raspberry Pi under topic "humidity"
  soil_humidity_stream.publish(soil_moisture);// publish to Raspberry Pi under topic "soil_humidity"
  delay(6000);
  // ignore these for now
//  if(pot_value > 500)
//    pi_led.publish("ledon");
//  else if(pot_value < 500)
//    pi_led.publish("ledoff");
    
  
}

// Function to connect and reconnect as necessary to the MQTT server.
// Should be called in the loop function and it will take care if connecting.
void MQTT_connect() {
  int8_t ret;

  // Stop if already connected.
  if (mqtt.connected()) {
    return;
  }

  Serial.print("Connecting to MQTT... ");

  uint8_t retries = 3;
  while ((ret = mqtt.connect()) != 0) { // connect will return 0 for connected
       Serial.println(mqtt.connectErrorString(ret));
       Serial.println("Retrying MQTT connection in 5 seconds...");
       mqtt.disconnect();
       delay(5000);  // wait 5 seconds
       retries--;
       if (retries == 0) {
         // basically die and wait for WDT to reset me
         while (1);
       }
  }
  Serial.println("MQTT Connected!");
}
