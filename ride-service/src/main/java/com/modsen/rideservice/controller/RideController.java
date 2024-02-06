package com.modsen.rideservice.controller;

import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Ride Controller", description = "Ride API")
public interface RideController {

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
    RidePageResponse getRidesPage(int page, int size, String orderBy);

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
    RidePageResponse getDriverRideHistory(int page, int size, String orderBy, UUID driverId);

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
    RidePageResponse getPassengerRideHistory(int page, int size, String orderBy, UUID passengerId);

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
    RideResponse getRideById(long id);

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
    ResponseEntity<RideResponse> createRide(
            CreateRideRequest createRequest,
            UriComponentsBuilder uriComponentsBuilder
    );

    @Operation(summary = "Delete a ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ride deleted"),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    void deleteRide(long id);

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
    RideResponse startRide(long id);

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
    RideResponse finishRide(long id);

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
                    }),
            @ApiResponse(responseCode = "503", description = "Driver service unavailable",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
    })
    DriverResponse getDriverProfile(long rideId);
}
