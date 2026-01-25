package com.kfu.crossplatform.dto;

import com.kfu.crossplatform.enums.EventType;
import com.kfu.crossplatform.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {
    private Long id;
    private Long sensorId;
    private EventType type;
    private String timestamp;
    private String description;
    private StatusType status;
    private List<String> photoUrls;
}
