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

    // CREATE
    @PostMapping
    public Sensor create(@RequestBody Sensor sensor) {
        return sensorService.create(sensor);
    }

    // READ ALL
    @GetMapping
    public List<Sensor> findAll() {
        return sensorService.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Sensor findById(@PathVariable Long id) {
        return sensorService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Sensor update(@PathVariable Long id,
                          @RequestBody Sensor sensor) {
        return sensorService.update(id, sensor);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sensorService.delete(id);
    }
}
