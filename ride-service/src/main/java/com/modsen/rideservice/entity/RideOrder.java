package com.modsen.rideservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class RideOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long passengerId;

    private Long driverId;

    private String startPoint;

    private String destinationPoint;

    private Status status;

    private LocalDateTime createdDate;

    private LocalDateTime acceptedDate;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private BigDecimal estimatedCost;
}
