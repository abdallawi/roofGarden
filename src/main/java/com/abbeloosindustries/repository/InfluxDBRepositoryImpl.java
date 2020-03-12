package com.abbeloosindustries.repository;

import com.abbeloosindustries.model.SensorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.query.internal.FluxResultMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InfluxDBRepositoryImpl implements InfluxDbCRUDRepository {

    private List<SensorData> sensorDataList = new ArrayList<>();
    private String jsonStr = "";

    // connectDatabase() makes a connection with the influxDB in the cloud
    private final InfluxDBClient connection = connectToCloudDatabase();

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = this.jsonStr.concat(jsonStr);
    }

    // make connection with the influx database
    private InfluxDBClient connectToCloudDatabase() {
        InfluxDBClient client = InfluxDBClientFactory.create(url, token);
        // Gives the status of the connection
        // !!!!!! seems to be necassary to read an or write to the influx db !!!!!!!!!!
        System.out.println(client.health());
        return client;
    }

    @Override
    public void readSensorData(String fluxQuery) {
        //writeSensorData();
        List<SensorData> sensorDataList = new ArrayList<>();
        QueryApi queryApi = client.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, organization);
        // Make a list of all the records in database RoofGarden
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                // Map every record in a POJO
                sensorDataList.add(makePojo(fluxRecord));
            }
        }
        this.sensorDataList = sensorDataList;
    }

    @Override
    public List<SensorData> getSensorData(String fluxQuery) {
        readSensorData(fluxQuery);
        return this.sensorDataList;
    }

    @Override
    public void writeSensorData() {

        try(WriteApi writeApi = connection.getWriteApi()) {
            Point point = Point.measurement("Sarren double test 2")
                    .addTag("location", "west")
                    .addField("value", 30.5);
            writeApi.writePoint(bucket, organization, point);
            System.out.println(connection.ready());
        }

    }

    @Override
    public void makeJson(SensorData sensorData) {
        ObjectMapper Obj = new ObjectMapper();

        try {
            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString(sensorData);
            // Displaying JSON String
            System.out.println(jsonStr);
            setJsonStr(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SensorData makePojo(FluxRecord fluxRecord) {
        // Use the fluxMapper to create a POJO
        FluxResultMapper mapper = new FluxResultMapper();
        SensorData sensorData = mapper.toPOJO(fluxRecord, SensorData.class);
        System.out.println(sensorData);
        return sensorData;
    }

    @Override
    public String getQuery(String query, String days, String sensorName) {

        System.out.println("in getquery method");
        String fluxQuery;
        switch (query) {
            case "all":
                fluxQuery = "from(bucket:\"Test\") |> range(start: -" + days + "d)" +
                        "  |> filter(fn: (r) => r._measurement == \"" + sensorName + "\")\n";
                break;
            case "max":
                fluxQuery = "from(bucket:\"Test\")\n" +
                        "  |> range(start: -" + days + "d)\n" +
                        "  |> filter(fn: (r) => r._measurement == \"" + sensorName + "\" and\n" +
                        "    r._field == \"some_level\")\n" +
                        "  |> max()";
                break;
            case "min":
                fluxQuery = "from(bucket:\"Test\")\n" +
                        "  |> range(start: -" + days + "d)\n" +
                        "  |> filter(fn: (r) => r._measurement == \"" + sensorName + "\" and\n" +
                        "    r._field == \"some_level\")\n" +
                        "  |> min()";
                break;
            case "average":
                fluxQuery = "from(bucket:\"Test\")\n" +
                        "  |> range(start: -" + days + "d)\n" +
                        "  |> filter(fn: (r) => r._measurement == \"" + sensorName + "\" and\n" +
                        "    r._field == \"some_level\")\n" +
                        "  |> mean()";
                break;
            case "last":
                fluxQuery = "from(bucket:\"Test\")\n" +
                        "  |> range(start: -" + days + "d)\n" +
                        "  |> filter(fn: (r) => r._measurement == \"" + sensorName + "\" and\n" +
                        "    r._field == \"some_level\")\n" +
                        "  |> last()";
                break;
            default:
                fluxQuery = "from(bucket:\"Test\") |> range(start: -1d)";
        }
        return fluxQuery;
    }
}
