package com.kfu.crossplatform.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.dto.AlertDTO;
import com.kfu.crossplatform.dto.CreateAlertRequest;
import com.kfu.crossplatform.dto.UpdateAlertRequest;
import com.kfu.crossplatform.service.AlertService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlertController {
     private final AlertService alertService;

     @GetMapping("/alerts")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<AlertDTO> getAllAlerts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
            return alertService.getAllPagedDTO(page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/alerts/{id}")
    public ResponseEntity<AlertDTO> getAlertById(@PathVariable Long id) {
        return alertService.getByIdDTO(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/alerts")
    public ResponseEntity<AlertDTO> createAlert(@Valid @RequestBody CreateAlertRequest request) {
        AlertDTO created = alertService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/alerts/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable Long id, @RequestBody UpdateAlertRequest request){
        return alertService.update(id, request)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    
    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id){
        boolean deleted = alertService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
