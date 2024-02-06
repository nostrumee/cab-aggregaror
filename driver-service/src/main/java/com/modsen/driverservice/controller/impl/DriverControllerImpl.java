package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.DriverController;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.modsen.driverservice.util.UriPaths.*;

@RestController
@RequestMapping(DRIVER_SERVICE_BASE_PATH)
@RequiredArgsConstructor
public class DriverControllerImpl implements DriverController {

    private final DriverService driverService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DriverPageResponse getDriverPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(name = "order_by", required = false) String orderBy
    ) {
        return driverService.getDriverPage(page, size, orderBy);
    }

    @GetMapping(GET_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public DriverResponse getDriverById(@PathVariable UUID id) {
        return driverService.getById(id);
    }

    @PostMapping
    @PreAuthorize("@driverServiceSecurityExpression.canSignUp()")
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest createRequest,
            @AuthenticationPrincipal Jwt jwt,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UUID externalId = UUID.fromString(jwt.getSubject());
        DriverResponse response = driverService.addDriver(createRequest, externalId);

        URI location = uriComponentsBuilder
                .path("api/v1/drivers/{id}")
                .build(Map.of("id", externalId));

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping(UPDATE_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@driverServiceSecurityExpression.canAccessDriver(#id)")
    public DriverResponse updateDriver(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDriverRequest updateRequest
    ) {
        return driverService.updateDriver(updateRequest, id);
    }

    @DeleteMapping(DELETE_DRIVER_BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@driverServiceSecurityExpression.canAccessDriver(#id)")
    public void deleteDriver(@PathVariable UUID id) {
        driverService.deleteDriver(id);
    }
}
