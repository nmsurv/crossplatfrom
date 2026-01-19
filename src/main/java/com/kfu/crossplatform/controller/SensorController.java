package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public Sensor create(@RequestBody Sensor sensor) {
        return sensorService.create(sensor);
    }


    @GetMapping
    public List<Sensor> findAll() {
        return sensorService.findAll();
    }
}
