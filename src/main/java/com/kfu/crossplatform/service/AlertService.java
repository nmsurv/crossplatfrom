package com.kfu.crossplatform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.repository.AlertRepository;

import jakarta.transaction.Transactional;

@Service
public class AlertService {
    
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository){
        this.alertRepository = alertRepository;
    }

    @Cacheable("alerts")
    public List<Alert> getAll(){
        return alertRepository.findAll();
    }

    public List<Alert> getAllBySensor(Long sensorId){
        return alertRepository.findAllBySensor_Id(sensorId);
    }

    @Cacheable(value = "alert", key = "#id")
    public Optional<Alert> getById(Long id){
        return alertRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "alert", allEntries = true)
    public Alert create(Alert alert){
        System.out.println("⚙️ Создание alert: " + alert);
        if (alert.getTimetamp() == null) {
            alert.setTimetamp(LocalDateTime.now());
        }
        return alertRepository.save(alert);
    }

    @Transactional
    @CacheEvict(value = "alert", key ="#id", allEntries = true)
    public Optional<Alert> update(Long id, Alert alertDetails){
        return alertRepository.findById(id).map(alert -> {
            alert.setSensor(alertDetails.getSensor());
            alert.setType(alertDetails.getType());
            alert.setTimetamp(LocalDateTime.now());
            alert.setDescription(alertDetails.getDescription());
            alert.setStatus(alertDetails.getStatus());
            alert.setPhotoUrls(alertDetails.getPhotoUrls());
            return alertRepository.save(alert);
        });
    }

    @Transactional
    @CacheEvict(value = "alert",key = "#id", allEntries = true)
    public boolean deleteById(Long id){
        if(alertRepository.existsById(id)) {
            alertRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Alert> getAllPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return alertRepository.findAll(pageable);
    }
}
