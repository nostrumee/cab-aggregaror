package com.modsen.driverservice.util;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaTypeMappings {
    public static final String JSON_DESERIALIZER_TYPE_MAPPINGS = String.format(
            "acceptRideMessage:%s",
            AcceptRideMessage.class.getName()
    );
}
