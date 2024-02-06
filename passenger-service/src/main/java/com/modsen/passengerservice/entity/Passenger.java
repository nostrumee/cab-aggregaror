package com.modsen.passengerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID externalId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    @Column(precision = 3, scale = 2, columnDefinition = "numeric")
    private Double rating = 5.0;
}
