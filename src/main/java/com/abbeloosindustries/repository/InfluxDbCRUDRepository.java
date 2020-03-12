package com.abbeloosindustries.repository;

import com.abbeloosindustries.model.SensorData;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfluxDbCRUDRepository {

    // Make the interface reusable --> use constructor from the class implementing the interface
    String bucket = "Test";
    String organization = "1724611cb04f2363";
    String url = "https://eu-central-1-1.aws.cloud2.influxdata.com";
    // Double test token
    char[] token = "e9-en5SF1HkWu-uIIOU-L6L42a4TE96UK98qhTxaE3-Chsai9Nq-eiRNAk4WPnxHEMzlAqSD0m_tpd4PZk4BBg==".toCharArray();
    //char[] token = "QBrxpfv5SG4mWWGiUIUgCFdrF5S339z3pruMzTjvo1Oc6Ygakd9HJXA4dZcRRhHGFtlImpUYInn9plzqAg2ylg==".toCharArray();
    // Make the connection final or use a method to create the connection ?
    InfluxDBClient client = InfluxDBClientFactory.create(url, token);

    // A read method with a given query
    void readSensorData(String fluxQuery);

    // to use the pojo's in thymeleaf
    List<SensorData> getSensorData(String fluxQuery);

    // Create and write new data to influx db through point protocol
    void writeSensorData();

    // Create a Json file
    void makeJson(SensorData sensorData);

    SensorData makePojo(FluxRecord fluxRecord);

    String getQuery(String query, String days, String sensorName);
}
