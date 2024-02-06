package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Passenger Controller", description = "Passenger API")
public interface PassengerController {

    @Operation(summary = "Get passenger page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passengers found",
                    content = {
                            @Content(schema = @Schema(implementation = PassengerPageResponse.class))
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
    PassengerPageResponse getPassengerPage(int page, int size, String orderBy);

    @Operation(summary = "Get a passenger by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger found",
                    content = {
                            @Content(schema = @Schema(implementation = PassengerResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    PassengerResponse getPassengerById(@PathVariable UUID id);

    @Operation(summary = "Create a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passenger created",
                    content = {
                            @Content(schema = @Schema(implementation = PassengerResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = {
                            @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Passenger already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
                    })
    })
    ResponseEntity<PassengerResponse> createPassenger(
            CreatePassengerRequest createRequest,
            Jwt jwt,
            UriComponentsBuilder uriComponentsBuilder
    );

    @Operation(summary = "Update an existing passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger updated",
                    content = {
                            @Content(schema = @Schema(implementation = PassengerResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = {
                            @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Passenger already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
                    })
    })
    PassengerResponse updatePassenger(UUID id, UpdatePassengerRequest updateRequest);

    @Operation(summary = "Delete a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger deleted"),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    void deletePassenger(UUID id);
}
