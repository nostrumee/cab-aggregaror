package com.modsen.ratingservice.conroller.impl;

import com.modsen.ratingservice.conroller.RatingController;
import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.modsen.ratingservice.util.UriPaths.*;

@RestController
@RequestMapping(RATING_SERVICE_BASE_PATH)
@RequiredArgsConstructor
public class RatingControllerImpl implements RatingController {

    private final RatingService ratingService;

    @PostMapping(RATE_DRIVER_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@ratingServiceSecurityExpression.canRateDriver(#ratingRequest.rideId)")
    public void rateDriver(
            @Valid @RequestBody DriverRatingRequest ratingRequest
    ) {
        ratingService.rateDriver(ratingRequest);
    }

    @PostMapping(RATE_PASSENGER_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@ratingServiceSecurityExpression.canRatePassenger(#ratingRequest.rideId)")
    public void ratePassenger(
            @Valid @RequestBody PassengerRatingRequest ratingRequest
    ) {
        ratingService.ratePassenger(ratingRequest);
    }
}
