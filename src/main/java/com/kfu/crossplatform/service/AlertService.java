package com.kfu.crossplatform.service;

import com.kfu.crossplatform.domain.User;  
import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.domain.Sensor;
import com.kfu.crossplatform.enums.StatusType;
import com.kfu.crossplatform.repository.AlertRepository;
import com.kfu.crossplatform.repository.SensorRepository;
import com.kfu.crossplatform.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    // Создание инцидента
    public Alert create(Alert alert, Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor id = " + sensorId + " not found"));

        alert.setId(null);
        alert.setSensor(sensor);
        alert.setTimestamp(LocalDateTime.now());
        alert.setStatus(StatusType.NEW);

        return alertRepository.save(alert);
    }

    // Получить все
    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    // Фильтр по статусу
    public List<Alert> findByStatus(StatusType status) {
        return alertRepository.findAllByStatus(status);
    }

    // Смена статуса
    public Alert changeStatus(Long alertId, StatusType status) {
        Alert alert = findById(alertId);
        alert.setStatus(status);
        return alertRepository.save(alert);
    }

    // Добавить фото
    public Alert addPhotos(Long alertId, List<String> photos) {
        Alert alert = findById(alertId);
        alert.getPhotoUrls().addAll(photos);
        return alertRepository.save(alert);
    }

    public Alert findById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert id = " + id + " not found"));
    }

    public Alert assignUser(Long alertId, Long userId) {
        Alert alert = findById(alertId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User id = " + userId + " not found"));

        alert.setAssignedTo(user);
        alert.setStatus(StatusType.IN_PROGRESS);

        return alertRepository.save(alert);
    }
}
