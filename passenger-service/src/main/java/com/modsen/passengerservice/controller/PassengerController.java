package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.*;
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
    public PassengerPageResponse getPassengerPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return passengerService.getPassengerPage(page, size, orderBy);
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
    public PassengerResponse getPassengerById(@PathVariable long id) {
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
            @ApiResponse(responseCode = "409", description = "Passenger already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
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
            @ApiResponse(responseCode = "409", description = "Passenger already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
                    })
    })
    public PassengerResponse updatePassenger(
            @PathVariable long id,
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
    public void deletePassenger(@PathVariable long id) {
        passengerService.deletePassenger(id);
    }

    @GetMapping("/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get passenger rating by passenger id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating found",
                    content = {
                            @Content(schema = @Schema(implementation = RatingResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public RatingResponse getPassengerRating(@PathVariable long id) {
        return passengerService.getPassengerRating(id);
    }
}
