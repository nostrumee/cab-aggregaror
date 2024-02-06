package com.modsen.ratingservice.conroller;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rating Controller", description = "Rating API")
public interface RatingController {

    @Operation(summary = "Rate a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver rated"),
            @ApiResponse(responseCode = "400", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "503", description = "Ride service unavailable",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    void rateDriver(DriverRatingRequest ratingRequest);

    @Operation(summary = "Rate a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger rated"),
            @ApiResponse(responseCode = "400", description = "Ride not found",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "409", description = "Invalid ride status",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "503", description = "Ride service unavailable",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    void ratePassenger(PassengerRatingRequest ratingRequest);
}
