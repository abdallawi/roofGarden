package com.abbeloosindustries.controller;

import com.abbeloosindustries.repository.InfluxDBRepositoryImpl;
import com.abbeloosindustries.repository.InfluxDbCRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/RoofGarden")
public class HomeController {

    private final InfluxDbCRUDRepository influxDbCRUDRepository;

    @Autowired
    public HomeController(InfluxDBRepositoryImpl influxDBRepositoryImpl) {
        this.influxDbCRUDRepository = influxDBRepositoryImpl;
    }

    @GetMapping({"", "/", "home"})
    public String home(Model model){

        model.addAttribute("lastTemp", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("last", "30", "temperature")).get(0));

        model.addAttribute("lastHum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("last", "30", "Humidity")).get(0));

        model.addAttribute("lastSoilHum", influxDbCRUDRepository.getSensorData(
                influxDbCRUDRepository.getQuery("last", "30", "Soil_humidity")).get(0));

        return "home";
    }

}
