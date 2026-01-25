package com.kfu.crossplatform.dto;

import com.kfu.crossplatform.enums.EventType;
import com.kfu.crossplatform.enums.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateAlertRequest {
    
    @NotNull(message = "Sensor ID is required")
    private Long sensorId;
    
    @NotNull(message = "Type is required")
    @Schema(description = "Alert type", example = "ACCIDENT", allowableValues = {"ACCIDENT", "HARD_BRAKING", "BUTTON"})
    private EventType type;
    
    private String description;
    
    @NotNull(message = "Status is required")
    @Schema(description = "Alert status", example = "NEW", allowableValues = {"NEW", "IN_PROGRESS", "RESOLVED"})
    private StatusType status;
    
    private List<String> photoUrls;
}
