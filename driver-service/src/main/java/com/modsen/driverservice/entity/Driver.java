package com.modsen.driverservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String licenceNumber;

    private String email;

    private String phone;

    @Column(precision = 3, scale = 2, columnDefinition = "numeric")
    private Double rating = 5.0;

    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.AVAILABLE;
}
