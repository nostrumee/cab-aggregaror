package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.*;
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

@Tag(name = "Driver Controller", description = "Driver API")
public interface DriverController {

    @Operation(summary = "Get driver page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drivers found",
                    content = {
                            @Content(schema = @Schema(implementation = DriverPageResponse.class))
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
    DriverPageResponse getDriverPage(int page, int size, String orderBy);

    @Operation(summary = "Get a driver by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver found",
                    content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Driver not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    DriverResponse getDriverById(UUID id);

    @Operation(summary = "Create a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Driver created",
                    content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = {
                            @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Driver already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
                    })
    })
    ResponseEntity<DriverResponse> createDriver(
            CreateDriverRequest createRequest,
            Jwt jwt,
            UriComponentsBuilder uriComponentsBuilder
    );

    @Operation(summary = "Update an existing driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver updated",
                    content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = {
                            @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Driver not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Driver already exists",
                    content = {
                            @Content(schema = @Schema(implementation = AlreadyExistsResponse.class))
                    })
    })
    DriverResponse updateDriver(UUID id, UpdateDriverRequest updateRequest);

    @Operation(summary = "Delete a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver deleted"),
            @ApiResponse(responseCode = "404", description = "Driver not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    void deleteDriver(UUID id);
}
