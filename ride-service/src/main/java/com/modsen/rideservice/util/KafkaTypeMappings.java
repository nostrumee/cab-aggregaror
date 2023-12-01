package com.modsen.rideservice.util;

import com.modsen.rideservice.dto.message.RideStatusMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTypeMappings {
    public final String JSON_SERIALIZER_TYPE_MAPPING = String.format(
            "rideStatusMessage:%s",
            RideStatusMessage.class.getName()
    );
}
