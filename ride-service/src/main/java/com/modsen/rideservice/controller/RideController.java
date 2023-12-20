package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.*;
import com.modsen.rideservice.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get rides page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rides found",
                    content = {
                            @Content(schema = @Schema(implementation = RidePageResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Page number must be equal or greater than 1",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid param",
                    content = {
                            @Content(schema = @Schema(implementation = ParamErrorResponse.class))
                    })
    })
    public RidePageResponse getRidesPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return rideService.getRidesPage(page, size, orderBy);
    }

    @GetMapping("/driver/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get driver's rides history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rides found",
                    content = {
                            @Content(schema = @Schema(implementation = RidePageResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Page number must be equal or greater than 1",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid param",
                    content = {
                            @Content(schema = @Schema(implementation = ParamErrorResponse.class))
                    })
    })
    public RidePageResponse getDriverRideHistory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy,
            @PathVariable long driverId
    ) {
        return rideService.getRidesByDriverId(driverId, page, size, orderBy);
    }

    @GetMapping("/passenger/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get passenger's rides history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rides found",
                    content = {
                            @Content(schema = @Schema(implementation = RidePageResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Page number must be equal or greater than 1",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid param",
                    content = {
                            @Content(schema = @Schema(implementation = ParamErrorResponse.class))
                    })
    })
    public RidePageResponse getPassengerRideHistory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy,
            @PathVariable long passengerId
    ) {
        return rideService.getRidesByPassengerId(passengerId, page, size, orderBy);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a ride by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride found",
                    content = {
                            @Content(schema = @Schema(implementation = RideResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public RideResponse getRideById(@PathVariable long id) {
        return rideService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ride created",
                    content = {
                            @Content(schema = @Schema(implementation = RideResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = {
                            @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ride deleted"),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public void deleteRide(@PathVariable long id) {
        rideService.deleteRide(id);
    }

    @GetMapping("/{id}/start")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Start a ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride started",
                    content = {
                            @Content(schema = @Schema(implementation = RideResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public RideResponse startRide(@PathVariable long id) {
        return rideService.startRide(id);
    }

    @GetMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Finish a ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride finished",
                    content = {
                            @Content(schema = @Schema(implementation = RideResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public RideResponse finishRide(@PathVariable long id) {
        return rideService.finishRide(id);
    }

    @GetMapping("/{rideId}/driver")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View driver's profile from a ride by ride id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver found",
                    content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Driver not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public DriverResponse getDriverProfile(@PathVariable long rideId) {
        return rideService.getDriverProfile(rideId);
    }
}
