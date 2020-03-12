package com.abbeloosindustries.controller;

import com.abbeloosindustries.repository.InfluxDBRepositoryImpl;
import com.abbeloosindustries.repository.InfluxDbCRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// When using time leaf you cant use annotation @RestConrtoller
@Controller
@RequestMapping("/RoofGarden")
public class TemperatureController {

    private final InfluxDbCRUDRepository influxDbCRUDRepository;

    @Autowired
    public TemperatureController(InfluxDBRepositoryImpl influxDBRepositoryImpl) {
        this.influxDbCRUDRepository = influxDBRepositoryImpl;
    }

    @GetMapping(value = "/TemperatureSensor/{query}") // What is "method=RequestMethod.GET"
    public String getInfluxdb(@PathVariable String query,
                              @RequestParam(required = false, defaultValue = "7") String days,
                              @RequestParam(required = false, defaultValue = "temperature") String sensorName,
                              Model model){
        model.addAttribute("lastTemp", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("last", "30", "temperature")).get(0));

        model.addAttribute("average", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("average", "30", "temperature")).get(0));

        model.addAttribute("maximum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("max", "30", "temperature")).get(0));

        model.addAttribute("minimum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("min", "30", "temperature")).get(0));

        String fluxQuery = influxDbCRUDRepository.getQuery(query, days, sensorName);
        model.addAttribute("memoryPointList", influxDbCRUDRepository.getSensorData(fluxQuery));

        return "temperature/list.html";
    }
}
