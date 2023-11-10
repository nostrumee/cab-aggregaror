package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.ErrorResponse;
import com.modsen.passengerservice.dto.response.PassengerListResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.response.ValidationErrorResponse;
import com.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
@Tag(name = "Passenger Controller", description = "Passenger API")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all passengers")
    @ApiResponse(responseCode = "200", description = "Passengers found")
    public PassengerListResponse getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PostMapping
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
            @ApiResponse(responseCode = "409", description = "Email or phone already taken",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
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
            @ApiResponse(responseCode = "409", description = "Email or phone already taken",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public PassengerResponse updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePassengerRequest updateRequest
    ) {
        return passengerService.updatePassenger(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger deleted"),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }
}
