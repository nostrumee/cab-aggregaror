package com.modsen.notificationservice.util;

import com.modsen.notificationservice.dto.message.RideStatusMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTypeMappings {
    public final String JSON_DESERIALIZER_TYPE_MAPPING = String.format(
            "rideStatusMessage:%s",
            RideStatusMessage.class.getName()
    );
}
