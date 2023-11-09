package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerListResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PassengerListResponse getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(
            @Valid @RequestBody CreatePassengerRequest createRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        PassengerResponse response = passengerService.addPassenger(createRequest);
        Long passengerId = response.id();

        URI location = uriComponentsBuilder
                .path("api/v1/passengers/{id}")
                .build(Map.of("id", passengerId));

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePassengerRequest updateRequest
    ) {
        return passengerService.updatePassenger(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }
}
