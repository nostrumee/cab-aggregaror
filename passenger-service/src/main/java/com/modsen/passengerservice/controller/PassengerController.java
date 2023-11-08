package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.passenger.CreatePassengerRequest;
import com.modsen.passengerservice.dto.passenger.PassengerListResponse;
import com.modsen.passengerservice.dto.passenger.PassengerResponse;
import com.modsen.passengerservice.dto.passenger.UpdatePassengerRequest;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
        Long id = response.id();

        return ResponseEntity
                .created(uriComponentsBuilder.path("api/v1/passengers/{id}").build(Map.of("id", id)))
                .body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PassengerResponse updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePassengerRequest updateRequest
    ) {
        return passengerService.updatePassenger(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }
}
