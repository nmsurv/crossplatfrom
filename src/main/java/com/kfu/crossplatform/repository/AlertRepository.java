package com.kfu.crossplatform.repository;

import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByStatus(StatusType status);
}
