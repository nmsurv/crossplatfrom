package com.kfu.crossplatform.service;

import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    // CREATE
    public Sensor create(Sensor sensor) {
        sensor.setId(null); // БД сама сгенерирует id
        return sensorRepository.save(sensor);
    }

    // READ ALL
    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    // READ BY ID
    public Sensor findById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor id = " + id + " не найден."));
    }

    // UPDATE
    public Sensor update(Long id, Sensor newSensor) {
        Sensor existing = findById(id);

        existing.setModel(newSensor.getModel());
        existing.setLocation(newSensor.getLocation());

        return sensorRepository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        sensorRepository.deleteById(id);
    }
}
