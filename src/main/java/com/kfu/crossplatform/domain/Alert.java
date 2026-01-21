package com.kfu.crossplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kfu.crossplatform.enums.EventType;
import com.kfu.crossplatform.enums.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связанный датчик
    @ManyToOne(fetch = FetchType.EAGER) // EAGER для простоты сериализации
    @JoinColumn(name = "sensor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Sensor sensor;

    // Тип события
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    // Время инцидента
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Описание
    @Column(length = 500)
    private String description;

    // Статус
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusType status;

    // Ответственный пользователь
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User assignedTo;

    // Фото (список URL)
    @ElementCollection
    @CollectionTable(
            name = "alert_photos",
            joinColumns = @JoinColumn(name = "alert_id")
    )
    @Column(name = "photo_url")
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();
}
