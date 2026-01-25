package com.kfu.crossplatform.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.kfu.crossplatform.enums.StatusType;
import com.kfu.crossplatform.enums.EventType;

import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Sensor cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @NotNull(message = "Event type is required")
    @Enumerated(EnumType.STRING)
    private EventType type;

    @NotNull(message = "Timetamp is required")
    private LocalDateTime timetamp;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ElementCollection
    @CollectionTable(name = "alert_photos", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;
    
}

