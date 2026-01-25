package com.kfu.crossplatform.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.dto.AlertDTO;
import com.kfu.crossplatform.dto.CreateAlertRequest;
import com.kfu.crossplatform.dto.UpdateAlertRequest;
import com.kfu.crossplatform.exeptions.ResourceNotFoundException;
import com.kfu.crossplatform.repository.AlertRepository;
import com.kfu.crossplatform.repository.SensorRepository;

import jakarta.transaction.Transactional;

@Service
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository){
        this.alertRepository = alertRepository;
        this.sensorRepository = sensorRepository;
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

    private AlertDTO convertToDTO(Alert alert) {
        String timestamp = alert.getTimetamp()
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        return new AlertDTO(
            alert.getId(),
            alert.getSensor().getId(),
            alert.getType(),
            timestamp,
            alert.getDescription(),
            alert.getStatus(),
            alert.getPhotoUrls()
        );
    }

    @Transactional
    @CacheEvict(value = "alert", allEntries = true)
    public AlertDTO create(CreateAlertRequest request){
        System.out.println("⚙️ Создание alert: " + request);
        Sensor sensor = sensorRepository.findById(request.getSensorId())
            .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + request.getSensorId()));
        
        Alert alert = new Alert();
        alert.setSensor(sensor);
        alert.setType(request.getType());
        alert.setTimetamp(LocalDateTime.now());
        alert.setDescription(request.getDescription());
        alert.setStatus(request.getStatus());
        alert.setPhotoUrls(request.getPhotoUrls());
        
        Alert saved = alertRepository.save(alert);
        return convertToDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "alert", key ="#id", allEntries = true)
    public Optional<AlertDTO> update(Long id, UpdateAlertRequest request){
        return alertRepository.findById(id).map(alert -> {
            Sensor sensor = sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + request.getSensorId()));
            
            alert.setSensor(sensor);
            alert.setType(request.getType());
            alert.setTimetamp(LocalDateTime.now());
            alert.setDescription(request.getDescription());
            alert.setStatus(request.getStatus());
            alert.setPhotoUrls(request.getPhotoUrls());
            return convertToDTO(alertRepository.save(alert));
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

    public List<AlertDTO> getAllPagedDTO(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return alertRepository.findAll(pageable).stream()
            .map(this::convertToDTO)
            .toList();
    }

    public Optional<AlertDTO> getByIdDTO(Long id){
        return alertRepository.findById(id).map(this::convertToDTO);
    }
}
