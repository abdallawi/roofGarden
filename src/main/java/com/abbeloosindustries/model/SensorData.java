package com.abbeloosindustries.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "Sensor")
public class SensorData {
    @Column(name = "result")
    private String result;

    @Column(name = "table")
    private Long table;

    @Column(name = "_start")
    private Instant startTime;

    @Column(name = "_stop")
    private Instant stoppedTime;

    @Column(name = "_time")
    private Instant totalTime;

    // when getting the average value needs to be double when getting max value needs to be Long
    // Maybe use double when adding data to the database
    @Column(name = "_value")
    private double value;

    @Column(name = "RoofGarden")
    private String roofGarden;

    @Column(name = "_field")
    private String field;

    @Column(name = "_measurement")
    private String name;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getTable() {
        return table;
    }

    public void setTable(Long table) {
        this.table = table;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getStoppedTime() {
        return stoppedTime;
    }

    public void setStoppedTime(Instant stoppedTime) {
        this.stoppedTime = stoppedTime;
    }

    public Instant getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Instant totalTime) {
        this.totalTime = totalTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getRoofGarden() {
        return roofGarden;
    }

    public void setRoofGarden(String roofGarden) {
        this.roofGarden = roofGarden;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
