package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.message.DriverStatusMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.DriverStatus;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.exception.InvalidRequestParamException;
import com.modsen.rideservice.exception.InvalidRideStatusException;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.mapper.MessageMapper;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.service.RideService;
import com.modsen.rideservice.service.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.modsen.rideservice.util.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final MessageMapper messageMapper;
    private final DriverService driverService;
    private final PassengerService passengerService;
    private final SendMessageHandler sendMessageHandler;

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public RidePageResponse getRidesByDriverId(long driverId, int page, int size, String orderBy) {
        log.info("Retrieving rides for driver with id {}", driverId);

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Ride> ridesPage = rideRepository.findAllByDriverIdAndStatus(driverId, RideStatus.FINISHED, pageRequest);

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
    @Transactional(readOnly = true)
    public RidePageResponse getRidesByPassengerId(long passengerId, int page, int size, String orderBy) {
        log.info("Retrieving rides for passenger with id {}", passengerId);

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Ride> ridesPage = rideRepository.findAllByPassengerIdAndStatus(passengerId, RideStatus.FINISHED, pageRequest);

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
    @Transactional(readOnly = true)
    public RideResponse getById(long id) {
        log.info("Retrieving ride by id {}", id);

        Ride ride = findRideById(id);
        return rideMapper.fromEntityToResponse(ride);
    }


    @Override
    @Transactional
    public RideResponse createRide(CreateRideRequest createRequest) {
        log.info("Creating ride order for passenger with id {}", createRequest.passengerId());

        passengerService.getPassengerById(createRequest.passengerId());

        Ride orderToCreate = rideMapper.fromCreateRequestToEntity(createRequest);
        orderToCreate.setStatus(RideStatus.CREATED);
        orderToCreate.setCreatedDate(LocalDateTime.now());
        orderToCreate.setEstimatedCost(getRideCost());

        Ride createdOrder = rideRepository.save(orderToCreate);
        CreateRideMessage orderMessage = CreateRideMessage.builder()
                .rideId(createdOrder.getId())
                .build();
        sendMessageHandler.handleCreateRideMessage(orderMessage);

        return rideMapper.fromEntityToResponse(createdOrder);
    }

    @Override
    @Transactional
    public void deleteRide(long id) {
        log.info("Deleting ride by id {}", id);

        Ride ride = findRideById(id);
        rideRepository.delete(ride);
    }

    @Override
    @Transactional
    public void acceptRide(AcceptRideMessage acceptRideMessage) {
        long rideId = acceptRideMessage.rideId();

        Ride ride = findRideById(rideId);
        PassengerResponse passenger = passengerService.getPassengerById(ride.getPassengerId());

        if (acceptRideMessage.driverId() == null) {
            log.info("Rejecting a ride with id {} as there no available drivers", rideId);

            ride.setStatus(RideStatus.REJECTED);
            rideRepository.save(ride);
        } else {
            log.info("Accepting ride order with id {} by driver with id {}", rideId, acceptRideMessage.driverId());

            ride.setDriverId(acceptRideMessage.driverId());
            ride.setStatus(RideStatus.ACCEPTED);
            ride.setAcceptedDate(LocalDateTime.now());
            rideRepository.save(ride);
        }

        RideStatusMessage rideStatusMessage =
                messageMapper.fromRideAndPassengerResponse(ride, passenger);
        sendMessageHandler.handleRideStatusMessage(rideStatusMessage);
    }

    @Override
    @Transactional
    public RideResponse startRide(long id) {
        log.info("Starting a ride with id {}", id);

        Ride rideToStart = findRideById(id);

        if (!rideToStart.getStatus().equals(RideStatus.ACCEPTED)) {
            log.error("Invalid ride status");

            List<RideStatus> validStatusList = Collections.singletonList(RideStatus.ACCEPTED);
            String statusNames = convertRideStatusListToString(validStatusList);

            throw new InvalidRideStatusException(statusNames);
        }

        PassengerResponse passenger = passengerService.getPassengerById(rideToStart.getPassengerId());

        rideToStart.setStatus(RideStatus.STARTED);
        rideToStart.setStartDate(LocalDateTime.now());
        Ride startedRide = rideRepository.save(rideToStart);

        RideStatusMessage rideStatusMessage =
                messageMapper.fromRideAndPassengerResponse(rideToStart, passenger);
        sendMessageHandler.handleRideStatusMessage(rideStatusMessage);

        return rideMapper.fromEntityToResponse(startedRide);
    }

    @Override
    @Transactional
    public RideResponse finishRide(long id) {
        log.info("Finishing a ride with id {}", id);

        Ride rideToFinish = findRideById(id);

        if (!rideToFinish.getStatus().equals(RideStatus.STARTED)) {
            log.error("Invalid ride status");

            List<RideStatus> validStatusList = Collections.singletonList(RideStatus.STARTED);
            String statusNames = convertRideStatusListToString(validStatusList);

            throw new InvalidRideStatusException(statusNames);
        }

        PassengerResponse passenger = passengerService.getPassengerById(rideToFinish.getPassengerId());

        rideToFinish.setStatus(RideStatus.FINISHED);
        rideToFinish.setFinishDate(LocalDateTime.now());
        Ride finishedRide = rideRepository.save(rideToFinish);

        DriverStatusMessage driverStatusMessage = DriverStatusMessage.builder()
                .driverId(rideToFinish.getDriverId())
                .status(DriverStatus.AVAILABLE)
                .build();
        sendMessageHandler.handleDriverStatusMessage(driverStatusMessage);

        RideStatusMessage rideStatusMessage =
                messageMapper.fromRideAndPassengerResponse(rideToFinish, passenger);
        sendMessageHandler.handleRideStatusMessage(rideStatusMessage);

        return rideMapper.fromEntityToResponse(finishedRide);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse getDriverProfile(long rideId) {
        log.info("Retrieving driver's profile from a ride with id {}", rideId);

        Ride ride = findRideById(rideId);
        if (ride.getDriverId() == null) {
            log.error("Invalid ride status");

            List<RideStatus> validStatusList = List.of(
                    RideStatus.ACCEPTED,
                    RideStatus.STARTED,
                    RideStatus.FINISHED
            );
            String statusNames = convertRideStatusListToString(validStatusList);

            throw new InvalidRideStatusException(statusNames);
        }

        long driverId = ride.getDriverId();
        return driverService.getDriverById(driverId);
    }

    private Ride findRideById(long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideNotFoundException(id);
                });
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

    private String convertRideStatusListToString(List<RideStatus> statuses) {
        return statuses.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
