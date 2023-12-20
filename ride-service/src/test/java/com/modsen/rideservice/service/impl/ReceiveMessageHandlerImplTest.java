package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.service.RideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.modsen.rideservice.util.TestUtils.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReceiveMessageHandlerImplTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private ReceiveMessageHandlerImpl receiveMessageHandler;

    @Test
    void handleAcceptRideMessage_shouldCallAcceptRideMethod() {
        var acceptRideMessage = getAcceptRideMessage(DEFAULT_ID);

        receiveMessageHandler.handleAcceptRideMessage(acceptRideMessage);

        verify(rideService).acceptRide(acceptRideMessage);
    }
}
