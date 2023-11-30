package com.modsen.notificationservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MailParams {
    public final String SUBJECT = "Ride #%d status update";
    public final String RIDE_ID_PARAM = "rideId";
    public final String FIRST_NAME_PARAM = "firstName";
    public final String STATUS_PARAM = "status";
    public final String START_POINT_PARAM = "startPoint";
    public final String DESTINATION_POINT_PARAM = "destinationPoint";
    public final String ESTIMATED_COST_PARAM = "estimatedCost";
    public final String RIDE_STATUS_EMAIL_TEMPLATE = "ridestatusemail.ftlh";
}
