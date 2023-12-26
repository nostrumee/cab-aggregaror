package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.*;
import com.modsen.driverservice.service.DriverService;
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

import static com.modsen.driverservice.util.UriPaths.*;

@RestController
@RequestMapping(DRIVER_SERVICE_BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Driver Controller", description = "Driver API")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    public DriverPageResponse getDriverPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return driverService.getDriverPage(page, size, orderBy);
    }

    @GetMapping(GET_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
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
    public DriverResponse getDriverById(@PathVariable long id) {
        return driverService.getById(id);
    }

    @PostMapping
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
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest createRequest,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        DriverResponse response = driverService.addDriver(createRequest);
        Long driverId = response.id();

        URI location = uriComponentsBuilder
                .path("api/v1/drivers/{id}")
                .build(Map.of("id", driverId));

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping(UPDATE_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
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
    public DriverResponse updateDriver(
            @PathVariable long id,
            @Valid @RequestBody UpdateDriverRequest updateRequest
    ) {
        return driverService.updateDriver(updateRequest, id);
    }

    @DeleteMapping(DELETE_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver deleted"),
            @ApiResponse(responseCode = "404", description = "Driver not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public void deleteDriver(@PathVariable long id) {
        driverService.deleteDriver(id);
    }
}
