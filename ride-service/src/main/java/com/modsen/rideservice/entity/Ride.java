package com.modsen.rideservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long passengerId;

    private Long driverId;

    private String startPoint;

    private String destinationPoint;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime acceptedDate;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private BigDecimal estimatedCost;
}
