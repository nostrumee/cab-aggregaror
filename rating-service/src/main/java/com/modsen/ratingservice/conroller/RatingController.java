package com.modsen.ratingservice.conroller;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/driver")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Rate a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver rated"),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public void rateDriver(
            @Valid @RequestBody DriverRatingRequest ratingRequest
    ) {
        ratingService.rateDriver(ratingRequest);
    }

    @PostMapping("/passenger")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Rate a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger rated"),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    public void ratePassenger(
            @Valid @RequestBody PassengerRatingRequest ratingRequest
    ) {
        ratingService.ratePassenger(ratingRequest);
    }

}
