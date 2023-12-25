package com.modsen.driverservice.util;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.dto.message.DriverStatusMessage;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import lombok.experimental.UtilityClass;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@UtilityClass
public class TestUtils {
    public final long DEFAULT_ID = 1L;
    public final String DEFAULT_FIRST_NAME = "John";
    public final String DEFAULT_LAST_NAME = "Doe";
    public final String DEFAULT_LICENCE_NUMBER = "123456789";
    public final String DEFAULT_EMAIL = "johndoe@example.com";
    public final String DEFAULT_PHONE = "123-45-67";
    public final double DEFAULT_RATING = 5.0;
    public final DriverStatus DEFAULT_STATUS = DriverStatus.AVAILABLE;

    public final long OTHER_ID = 11L;
    public final String OTHER_FIRST_NAME = "Amber";
    public final String OTHER_LAST_NAME = "Lewis";
    public final String OTHER_LICENCE_NUMBER = "111222333";
    public final String OTHER_EMAIL = "amberlewis@example.com";
    public final String OTHER_PHONE = "111-22-33";
    public final double OTHER_RATING = 4.65;
    public final DriverStatus OTHER_STATUS = DriverStatus.UNAVAILABLE;

    public final int VALID_PAGE = 1;
    public final int VALID_SIZE = 2;
    public final String VALID_ORDER_BY = "firstName";

    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "invalidOrderBy";
    public final String PAGE_PARAM_OF_INVALID_TYPE = "invalidType";

    public final String INVALID_EMAIL = "invalid@email";
    public final String INVALID_PHONE = "111111111";
    public final String INVALID_LICENCE_NUMBER = "112233";

    public final long NON_EXISTING_ID = 256;
    public final long EXISTING_ID = 2;
    public final long TOTAL = 10L;

    public final String FIRST_NAME_VALIDATION_MESSAGE_KEY = "firstname.not.blank";
    public final String LAST_NAME_VALIDATION_MESSAGE_KEY = "lastname.not.blank";
    public final String LICENCE_NUMBER_VALIDATION_MESSAGE_KEY = "licence-number.valid";
    public final String EMAIL_VALIDATION_MESSAGE_KEY = "email.valid";
    public final String PHONE_VALIDATION_MESSAGE_KEY = "phone.valid";

    public final String FIRST_NAME_FIELD_NAME = "firstName";
    public final String LAST_NAME_FIELD_NAME = "lastName";
    public final String LICENCE_NUMBER_FIELD_NAME = "licenceNumber";
    public final String EMAIL_FIELD_NAME = "email";
    public final String PHONE_FIELD_NAME = "phone";

    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String ORDER_BY_PARAM_NAME = "order_by";
    public final String ID_PARAM_NAME = "id";

    public final String DRIVER_RATING_TOPIC_NAME_KEY = "driver-rating-topic-name";
    public final String DRIVER_STATUS_TOPIC_NAME_KEY = "driver-status-topic-name";
    public final String CREATE_RIDE_TOPIC_NAME_KEY = "create-ride-topic-name";
    public final String ACCEPT_RIDE_TOPIC_NAME_KEY = "accept-ride-topic-name";

    public final String POSTGRES_IMAGE_NAME_KEY = "postgres-image-name";
    public final String KAFKA_IMAGE_NAME_KEY = "kafka-image-name";

    public Driver getDefaultDriver() {
        return Driver.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
                .status(DEFAULT_STATUS)
                .build();
    }

    public Driver getOtherDriver() {
        return Driver.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(OTHER_RATING)
                .status(OTHER_STATUS)
                .build();
    }

    public Driver getNotSavedDriver() {
        return Driver.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public DriverResponse getDefaultDriverResponse() {
        return DriverResponse.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
                .status(DEFAULT_STATUS)
                .build();
    }

    public DriverResponse getOtherDriverResponse() {
        return DriverResponse.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(OTHER_RATING)
                .status(OTHER_STATUS)
                .build();
    }

    public DriverResponse getUpdatedDriverResponse() {
        return DriverResponse.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(DEFAULT_RATING)
                .status(DriverStatus.AVAILABLE)
                .build();
    }

    public CreateDriverRequest getCreateDriverRequest() {
        return CreateDriverRequest.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public UpdateDriverRequest getUpdateDriverRequest() {
        return UpdateDriverRequest.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();
    }

    public DriverRatingMessage getDriverRatingMessage() {
        return DriverRatingMessage.builder()
                .driverId(DEFAULT_ID)
                .rating(OTHER_RATING)
                .build();
    }

    public DriverStatusMessage getDriverStatusMessage() {
        return DriverStatusMessage.builder()
                .driverId(DEFAULT_ID)
                .status(DriverStatus.UNAVAILABLE)
                .build();
    }

    public AcceptRideMessage getAcceptRideMessage() {
        return AcceptRideMessage.builder()
                .rideId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
                .build();
    }

    public List<Driver> getDriverList() {
        return Arrays.asList(
                getOtherDriver(),
                getDefaultDriver()
        );
    }

    public List<DriverResponse> getDriverResponseList() {
        return Arrays.asList(
                getOtherDriverResponse(),
                getDefaultDriverResponse()
        );
    }

    public Page<Driver> getDriverPage() {
        return new PageImpl<>(getDriverList());
    }

    public PageRequest getPageRequest(int page, int size, String orderBy) {
        return PageRequest.of(page - 1, size, Sort.by(orderBy));
    }

    public KafkaProducer<String, Object> getKafkaProducer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer<>(properties);
    }

    public KafkaConsumer<String, Object> getKafkaConsumer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new KafkaConsumer<>(properties);
    }
}
