package com.kfu.crossplatform.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @NotBlank(message = "model cannot be blank")
    private String model;
    
    @NotBlank(message = "local cannot be blank")
    private String location;

    @NotNull(message = "user is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User assingnedTo;
}
