package com.modsen.notificationservice.exception;

import static com.modsen.notificationservice.util.ErrorMessages.*;

public class RideStatusTransformException extends RuntimeException {

    public RideStatusTransformException(long id, String reason) {
        super(String.format(RIDE_STATUS_TRANSFORM_ERROR_MESSAGE, id, reason));
    }
}
