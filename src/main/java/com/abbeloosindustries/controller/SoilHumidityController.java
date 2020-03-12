package com.abbeloosindustries.controller;

import com.abbeloosindustries.repository.InfluxDBRepositoryImpl;
import com.abbeloosindustries.repository.InfluxDbCRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/RoofGarden")
public class SoilHumidityController {


    private final InfluxDbCRUDRepository influxDbCRUDRepository;

    @Autowired
    public SoilHumidityController(InfluxDBRepositoryImpl influxDBRepositoryImpl) {
        this.influxDbCRUDRepository = influxDBRepositoryImpl;
    }

    @GetMapping(value = "/SoilHumiditySensor") // What is "method=RequestMethod.GET"
    public String getInfluxdb(@RequestParam(required = false, defaultValue = "last") String query,
                              @RequestParam(required = false, defaultValue = "7") String days,
                              @RequestParam(required = false, defaultValue = "Soil_humidity") String sensorName,
                              Model model){
        model.addAttribute("last", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("last", "30", "Soil_humidity")).get(0));

        model.addAttribute("average", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("average", "30", "Soil_humidity")).get(0));

        model.addAttribute("maximum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("max", "30", "Soil_humidity")).get(0));

        model.addAttribute("minimum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("min", "30", "Soil_humidity")).get(0));

        String fluxQuery = influxDbCRUDRepository.getQuery(query, days, sensorName);
        model.addAttribute("memoryPointList", influxDbCRUDRepository.getSensorData(fluxQuery));

        return "soil_humidity/list.html";
    }
}
