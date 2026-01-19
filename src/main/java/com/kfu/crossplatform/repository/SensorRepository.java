package com.kfu.crossplatform.repository;

import com.kfu.crossplatform.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}