package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.domain.Alert;
import com.kfu.crossplatform.dto.AssignUserRequest;
import com.kfu.crossplatform.enums.StatusType;
import com.kfu.crossplatform.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    // POST /api/incidents?sensorId=1
    @PostMapping
    public Alert create(@RequestParam Long sensorId,
                        @RequestBody Alert alert) {
        return alertService.create(alert, sensorId);
    }

    // GET /api/incidents
    // GET /api/incidents?status=NEW
    @GetMapping
    public List<Alert> findAll(@RequestParam(required = false) StatusType status) {
        if (status != null) {
            return alertService.findByStatus(status);
        }
        return alertService.findAll();
    }

    // PUT /api/incidents/{id}/status_change?status=RESOLVED
    @PutMapping("/{id}/status_change")
    public Alert changeStatus(@PathVariable Long id,
                              @RequestParam StatusType status) {
        return alertService.changeStatus(id, status);
    }

    // POST /api/incidents/{id}/photos
    @PostMapping("/{id}/photos")
    public Alert uploadPhotos(@PathVariable Long id,
                              @RequestBody List<String> photoUrls) {
        return alertService.addPhotos(id, photoUrls);
    }

    @PutMapping("/{id}/assign")
    public Alert assign(@PathVariable Long id,
                    @RequestBody AssignUserRequest request) {
    return alertService.assignUser(id, request.getUserId());
    }
}
