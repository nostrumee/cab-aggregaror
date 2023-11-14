package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.request.AcceptRideOrderRequest;
import com.modsen.rideservice.dto.request.CreateRideOrderRequest;
import com.modsen.rideservice.dto.response.RideOrderResponse;
import com.modsen.rideservice.entity.RideOrder;
import com.modsen.rideservice.entity.Status;
import com.modsen.rideservice.exception.RideOrderNotFoundException;
import com.modsen.rideservice.mapper.RideOrderMapper;
import com.modsen.rideservice.repository.RideOrderRepository;
import com.modsen.rideservice.service.RideOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideOrderServiceImpl  implements RideOrderService {

    private final RideOrderRepository rideOrderRepository;
    private final RideOrderMapper rideOrderMapper;

    @Override
    public RideOrderResponse createRideOrder(CreateRideOrderRequest createRequest) {
        log.info("Creating ride order");

        RideOrder orderToCreate = rideOrderMapper.fromCreateRequestToEntity(createRequest);
        orderToCreate.setStatus(Status.CREATED);
        orderToCreate.setCreatedDate(LocalDateTime.now());

        RideOrder createdOrder = rideOrderRepository.save(orderToCreate);

        return rideOrderMapper.fromEntityToResponse(createdOrder);
    }

    @Override
    public RideOrderResponse acceptRideOrder(AcceptRideOrderRequest acceptRequest) {
        long rideId = acceptRequest.rideId();
        long driverId = acceptRequest.driverId();

        log.info("Accepting ride order with id {} by driver with id {}", rideId, driverId);

        RideOrder orderToAccept = rideOrderRepository.findById(acceptRequest.rideId())
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", rideId);
                    return new RideOrderNotFoundException(rideId);
                });

        orderToAccept.setStatus(Status.ACCEPTED);
        orderToAccept.setAcceptedDate(LocalDateTime.now());
        RideOrder acceptedOrder = rideOrderRepository.save(orderToAccept);

        return rideOrderMapper.fromEntityToResponse(acceptedOrder);
    }

    @Override
    public RideOrderResponse startRide(long id) {
        log.info("Starting a ride with id {}", id);

        RideOrder orderToStart = rideOrderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideOrderNotFoundException(id);
                });

        orderToStart.setStatus(Status.STARTED);
        orderToStart.setStartDate(LocalDateTime.now());
        RideOrder startedOrder = rideOrderRepository.save(orderToStart);

        return rideOrderMapper.fromEntityToResponse(startedOrder);
    }

    @Override
    public RideOrderResponse finishRide(long id) {
        log.info("Finishing a ride with id {}", id);

        RideOrder orderToFinish = rideOrderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ride order with id {} was not found", id);
                    return new RideOrderNotFoundException(id);
                });

        orderToFinish.setStatus(Status.FINISHED);
        orderToFinish.setFinishDate(LocalDateTime.now());
        RideOrder finishedOrder = rideOrderRepository.save(orderToFinish);

        return rideOrderMapper.fromEntityToResponse(finishedOrder);
    }
}
