package com.kfu.crossplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateSensorRequest {
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Assigned to user ID is required")
    private Long assignedToId;
}
