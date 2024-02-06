package com.modsen.passengerservice.controller.impl;

import com.modsen.passengerservice.controller.PassengerController;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static com.modsen.passengerservice.util.UriPaths.*;

@RestController
@RequestMapping(PASSENGER_SERVICE_BASE_PATH)
@RequiredArgsConstructor
public class PassengerControllerImpl implements PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PassengerPageResponse getPassengerPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return passengerService.getPassengerPage(page, size, orderBy);
    }

    @GetMapping(GET_PASSENGER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse getPassengerById(@PathVariable long id) {
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

    @PutMapping(UPDATE_PASSENGER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public PassengerResponse updatePassenger(
            @PathVariable long id,
            @Valid @RequestBody UpdatePassengerRequest updateRequest
    ) {
        return passengerService.updatePassenger(updateRequest, id);
    }

    @DeleteMapping(DELETE_PASSENGER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable long id) {
        passengerService.deletePassenger(id);
    }
}
