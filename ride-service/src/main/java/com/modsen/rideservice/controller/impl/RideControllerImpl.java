package com.modsen.rideservice.controller.impl;

import com.modsen.rideservice.controller.RideController;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.modsen.rideservice.util.UriPaths.*;

@RestController
@RequestMapping(RIDE_SERVICE_BASE_PATH)
@RequiredArgsConstructor
public class RideControllerImpl implements RideController {

    private final RideService rideService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RidePageResponse getRidesPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return rideService.getRidesPage(page, size, orderBy);
    }

    @GetMapping(GET_DRIVER_RIDE_HISTORY_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canAccessRideHistory(#driverId)")
    public RidePageResponse getDriverRideHistory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy,
            @PathVariable UUID driverId
    ) {
        return rideService.getRidesByDriverId(driverId, page, size, orderBy);
    }

    @GetMapping(GET_PASSENGER_RIDE_HISTORY_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canAccessRideHistory(#passengerId)")
    public RidePageResponse getPassengerRideHistory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy,
            @PathVariable UUID passengerId
    ) {
        return rideService.getRidesByPassengerId(passengerId, page, size, orderBy);
    }

    @GetMapping(GET_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canAccessRide(#id)")
    public RideResponse getRideById(@PathVariable long id) {
        return rideService.getById(id);
    }

    @PostMapping
    public ResponseEntity<RideResponse> createRide(
            @Valid @RequestBody CreateRideRequest createRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        RideResponse response = rideService.createRide(createRequest);
        Long rideId = response.id();

        URI location = uriComponentsBuilder
                .path("api/v1/rides/{id}")
                .build(Map.of("id", rideId));

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @DeleteMapping(DELETE_BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable long id) {
        rideService.deleteRide(id);
    }

    @GetMapping(START_RIDE_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canChangeRideStatus(#id)")
    public RideResponse startRide(@PathVariable long id) {
        return rideService.startRide(id);
    }

    @GetMapping(FINISH_RIDE_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canChangeRideStatus(#id)")
    public RideResponse finishRide(@PathVariable long id) {
        return rideService.finishRide(id);
    }

    @GetMapping(GET_DRIVER_PROFILE_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@rideServiceSecurityExpression.canAccessRide(#rideId)")
    public DriverResponse getDriverProfile(@PathVariable long rideId) {
        return rideService.getDriverProfile(rideId);
    }
}
