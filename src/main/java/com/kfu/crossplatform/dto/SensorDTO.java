package com.kfu.crossplatform.dto;

public record SensorDTO(
    Long id,
    String model,
    String location,
    String assignedToUsername
) { }
