package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.PassengerDTO;
import com.modsen.passengerservice.dto.PassengerListDTO;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public PassengerListDTO getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/{id}")
    public PassengerDTO getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PassengerDTO> createPassenger(
            @RequestBody PassengerDTO passengerDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long id = passengerService.addPassenger(passengerDTO);
        PassengerDTO passenger = passengerService.getById(id);

        return ResponseEntity
                .created(uriComponentsBuilder.path("api/v1/passengers/{id}").build(Map.of("id", id)))
                .body(passenger);
    }

    @PatchMapping("/{id}")
    public PassengerDTO updatePassenger(@PathVariable Long id, @RequestBody PassengerDTO passengerDTO) {
        return passengerService.updatePassenger(passengerDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }
}
