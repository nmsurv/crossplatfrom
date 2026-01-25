package com.kfu.crossplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kfu.crossplatform.domain.Alert;

@Repository
public interface AlertRepository extends
    JpaRepository<Alert, Long>{
    List<Alert> findAllBySensor_Id(Long sensorId);
}
