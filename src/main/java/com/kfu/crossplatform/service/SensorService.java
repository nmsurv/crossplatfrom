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
import com.kfu.crossplatform.domain.User;
import com.kfu.crossplatform.dto.CreateSensorRequest;
import com.kfu.crossplatform.dto.UpdateSensorRequest;
import com.kfu.crossplatform.exeptions.ResourceNotFoundException;
import com.kfu.crossplatform.repository.SensorRepository;
import com.kfu.crossplatform.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    public SensorService(SensorRepository sensorRepository, UserRepository userRepository){
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository;
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
    public Sensor create(CreateSensorRequest request){
        User user = userRepository.findById(request.getAssignedToId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToId()));
        
        Sensor sensor = new Sensor();
        sensor.setModel(request.getModel());
        sensor.setLocation(request.getLocation());
        sensor.setAssingnedTo(user);
        return sensorRepository.save(sensor);
    }

    @Transactional
    @CacheEvict(value = "sensor", key ="#id", allEntries = true)
    public Optional<Sensor> update(Long id, UpdateSensorRequest request){
        return sensorRepository.findById(id).map(sensor -> {
            User user = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToId()));
            
            sensor.setModel(request.getModel());
            sensor.setLocation(request.getLocation());
            sensor.setAssingnedTo(user);
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
