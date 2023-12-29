package com.modsen.rideservice.util;

import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTypeMappings {
    public final String JSON_SERIALIZER_TYPE_MAPPINGS = String.format(
            "rideStatusMessage:%s",
            RideStatusMessage.class.getName()
    );
    public final String JSON_DESERIALIZER_TYPE_MAPPINGS = String.format(
            "acceptRideMessage:%s",
            AcceptRideMessage.class.getName()
    );
}
