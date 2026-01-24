package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("/sensors")
    public Page<Sensor> getAllSensors(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
            return sensorService.getAllPaged(page, size);
        }
    
    @GetMapping("/sensors/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        return sensorService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/sensors")
    public ResponseEntity<Sensor> createSensor(@Valid  @RequestBody Sensor sensor) {
        Sensor created = sensorService.create(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/sensors/{id}")
    public ResponseEntity<Sensor> updateSensor(@Valid @PathVariable Long id, @RequestBody Sensor sensor){
        return sensorService.update(id, sensor)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id){
        boolean deleted = sensorService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
