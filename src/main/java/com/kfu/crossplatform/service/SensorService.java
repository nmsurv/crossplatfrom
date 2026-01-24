package com.kfu.crossplatform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.repository.SensorRepository;

import jakarta.transaction.Transactional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository){
        this.sensorRepository = sensorRepository;
    }

    @Cacheable("sensors")
    public List<Sensor> getAll(){
        return sensorRepository.findAll();
    }

    public List<Sensor> getAllByModel(String model){
        return sensorRepository.findAllByModel(model);
    }

    @Cacheable(value = "sensor", key = "#id")
    public Optional<Sensor> getById(Long id){
        return sensorRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "sensor", allEntries = true)
    public Sensor create(Sensor sensor){
        return sensorRepository.save(sensor);
    }

    @Transactional
    @CacheEvict(value = "sensor", key ="#id", allEntries = true)
    public Optional<Sensor> update(Long id, Sensor sensorDetails){
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setModel(sensorDetails.getModel());
            sensor.setLocation(sensorDetails.getLocation());
            sensor.setAssingnedTo(sensorDetails.getAssingnedTo());
            return sensorRepository.save(sensor);
        });
    }

    @Transactional
    @CacheEvict(value = "sensor", key="#id", allEntries = true)
    public boolean deleteById(Long id){
        if(sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Sensor> getAllPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return sensorRepository.findAll(pageable);
    }
}
