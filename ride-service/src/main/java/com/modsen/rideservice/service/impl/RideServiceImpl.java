package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.message.PassengerRatingMessage;
import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.message.RideOrderMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.request.RatingRequest;
import com.modsen.rideservice.dto.message.DriverRatingMessage;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.Status;
import com.modsen.rideservice.exception.InvalidRequestParamException;
import com.modsen.rideservice.exception.RideNotFinishedException;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.modsen.rideservice.util.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    @Override
    public RidePageResponse getRidesPage(int page, int size, String orderBy) {
        log.info("Retrieving rides page");

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Ride> ridesPage = rideRepository.findAll(pageRequest);

        List<Ride> retrievedRides = ridesPage.getContent();
        Long total = ridesPage.getTotalElements();

        List<RideResponse> rides =
                rideMapper.fromEntityListToResponseList(retrievedRides);

        return RidePageResponse.builder()
                .rides(rides)
                .pageNumber(page)
                .total(total)
                .build();
    }

    @Override
    public RidePageResponse getRidesByDriverId(long driverId, int page, int size, String orderBy) {
        log.info("Retrieving rides for driver with id {}", driverId);

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Ride> ridesPage = rideRepository.findAllByDriverIdAndStatus(driverId, Status.FINISHED, pageRequest);

        List<Ride> retrievedRides = ridesPage.getContent();
        Long total = ridesPage.getTotalElements();

        List<RideResponse> rides =
                rideMapper.fromEntityListToResponseList(retrievedRides);

        return RidePageResponse.builder()
                .rides(rides)
                .pageNumber(page)
                .total(total)
                .build();
    }

    @Override
    public RidePageResponse getRidesByPassengerId(long passengerId, int page, int size, String orderBy) {
        log.info("Retrieving rides for passenger with id {}", passengerId);

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Ride> ridesPage = rideRepository.findAllByPassengerIdAndStatus(passengerId, Status.FINISHED, pageRequest);

        List<Ride> retrievedRides = ridesPage.getContent();
        Long total = ridesPage.getTotalElements();

        List<RideResponse> rides =
                rideMapper.fromEntityListToResponseList(retrievedRides);

        return RidePageResponse.builder()
                .rides(rides)
                .pageNumber(page)
                .total(total)
                .build();
    }

    @Override
    public RideResponse getById(long id) {
        log.info("Retrieving ride by id {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        return rideMapper.fromEntityToResponse(ride);
    }


    @Override
    public RideResponse createRide(CreateRideRequest createRequest) {
        log.info("Creating ride order");

        Ride orderToCreate = rideMapper.fromCreateRequestToEntity(createRequest);
        orderToCreate.setStatus(Status.CREATED);
        orderToCreate.setCreatedDate(LocalDateTime.now());
        orderToCreate.setEstimatedCost(getRideCost());

        Ride createdOrder = rideRepository.save(orderToCreate);

        RideOrderMessage orderMessage = RideOrderMessage.builder()
                .rideId(createdOrder.getId())
                .build();
        // TODO: send message to notification-service about created order
        // TODO: send order message to 'ride-order' topic

        return rideMapper.fromEntityToResponse(createdOrder);
    }

    @Override
    public void deleteRide(long id) {
        log.info("Deleting ride by id {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        rideRepository.delete(ride);
    }

    @Override
    public RideResponse acceptRide(AcceptRideMessage acceptRequest) {
        long rideId = acceptRequest.rideId();
        long driverId = acceptRequest.driverId();

        log.info("Accepting ride order with id {} by driver with id {}", rideId, driverId);

        Ride orderToAccept = rideRepository.findById(acceptRequest.rideId())
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", rideId);
                    return new RideNotFoundException(rideId);
                });

        orderToAccept.setStatus(Status.ACCEPTED);
        orderToAccept.setAcceptedDate(LocalDateTime.now());
        Ride acceptedOrder = rideRepository.save(orderToAccept);

        // TODO: send message to notification-service about accepted order

        return rideMapper.fromEntityToResponse(acceptedOrder);
    }

    @Override
    public RideResponse startRide(long id) {
        log.info("Starting a ride with id {}", id);

        Ride rideToStart = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        rideToStart.setStatus(Status.STARTED);
        rideToStart.setStartDate(LocalDateTime.now());
        Ride startedRide = rideRepository.save(rideToStart);

        // TODO: send message to notification-service about started ride

        return rideMapper.fromEntityToResponse(startedRide);
    }

    @Override
    public RideResponse finishRide(long id) {
        log.info("Finishing a ride with id {}", id);

        Ride rideToFinish = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        rideToFinish.setStatus(Status.FINISHED);
        rideToFinish.setFinishDate(LocalDateTime.now());
        Ride finishedRide = rideRepository.save(rideToFinish);

        // TODO: send message to notification-service about finished ride
        // TODO: send message to driver-service to make driver status available

        return rideMapper.fromEntityToResponse(finishedRide);
    }

    @Override
    public void rateDriver(RatingRequest ratingRequest, long id) {
        log.info("Rating a driver of ride with id {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        if (!ride.getStatus().equals(Status.FINISHED)) {
            log.error("Ride order with id {} is not finished yet", id);
            throw new RideNotFinishedException(id);
        }

        DriverRatingMessage ratingMessage = DriverRatingMessage.builder()
                .rideId(id)
                .driverId(ride.getDriverId())
                .rating(ratingRequest.rating())
                .build();
        // TODO: send rating message to 'rate-driver' topic
    }

    @Override
    public void ratePassenger(RatingRequest ratingRequest, long id) {
        log.info("Rating a passenger of ride with id {}", id);

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideNotFoundException(id);
                });

        if (ride.getStatus().equals(Status.FINISHED)) {
            log.error("Ride order with id {} is not finished yet", id);
            throw new RideNotFinishedException(id);
        }

        PassengerRatingMessage ratingMessage = PassengerRatingMessage.builder()
                .rideId(id)
                .passengerId(ride.getDriverId())
                .rating(ratingRequest.rating())
                .build();
        // TODO: send rating message to 'rate-passenger' topic
    }

    @Override
    public DriverResponse getDriverProfile(long rideId) {
        // TODO: call to driver-service to fetch driver profile
        return null;
    }

    private PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            log.error("Invalid request parameter passed: page: {}, size: {}", page, size);
            throw new InvalidRequestParamException(INVALID_PAGE_PARAMETERS_MESSAGE);
        }

        PageRequest pageRequest;
        if (orderBy == null) {
            pageRequest = PageRequest.of(page - 1, size);
        } else {
            validateSortingParameter(orderBy);
            pageRequest = PageRequest.of(page - 1, size, Sort.by(orderBy));
        }

        return pageRequest;
    }

    private void validateSortingParameter(String orderBy) {
        List<String> fieldNames = Arrays.stream(RideResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        if (!fieldNames.contains(orderBy)) {
            String acceptableParams = String.join(", ", fieldNames);
            String errorMessage = String.format(INVALID_SORTING_PARAMETER_MESSAGE, orderBy, acceptableParams);
            throw new InvalidRequestParamException(errorMessage);
        }
    }

    private BigDecimal getRideCost() {
        BigDecimal min = BigDecimal.valueOf(3.0);
        BigDecimal max = BigDecimal.valueOf(20.0);

        return min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)))
                .setScale(1, RoundingMode.HALF_UP);
    }
}
