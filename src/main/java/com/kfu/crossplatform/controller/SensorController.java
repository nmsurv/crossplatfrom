package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.dto.CreateSensorRequest;
import com.kfu.crossplatform.dto.SensorDTO;
import com.kfu.crossplatform.dto.UpdateSensorRequest;
import com.kfu.crossplatform.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("/sensors")
    public List<SensorDTO> getAllSensors(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
            return sensorService.getAllPaged(page, size)
                .map(sensor -> new SensorDTO(
                    sensor.getId(),
                    sensor.getModel(),
                    sensor.getLocation(),
                    sensor.getAssingnedTo().getUsername()
                ))
                .getContent();
        }
    
    @GetMapping("/sensors/{id}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable Long id) {
        return sensorService.getById(id)
            .map(sensor -> ResponseEntity.ok(new SensorDTO(
                sensor.getId(),
                sensor.getModel(),
                sensor.getLocation(),
                sensor.getAssingnedTo().getUsername()
            )))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/sensors")
    public ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody CreateSensorRequest request) {
        Sensor created = sensorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SensorDTO(
            created.getId(),
            created.getModel(),
            created.getLocation(),
            created.getAssingnedTo().getUsername()
        ));
    }
    
    @PutMapping("/sensors/{id}")
    public ResponseEntity<SensorDTO> updateSensor(@Valid @PathVariable Long id, @RequestBody UpdateSensorRequest request){
        return sensorService.update(id, request)
            .map(sensor -> ResponseEntity.ok(new SensorDTO(
                sensor.getId(),
                sensor.getModel(),
                sensor.getLocation(),
                sensor.getAssingnedTo().getUsername()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id){
        boolean deleted = sensorService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
