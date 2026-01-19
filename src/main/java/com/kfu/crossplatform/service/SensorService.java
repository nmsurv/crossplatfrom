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

    public Sensor create(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }
}
