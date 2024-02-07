package com.modsen.driverservice.component.step;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.CreateRideMessage;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.RideOrderService;
import com.modsen.driverservice.service.impl.DriverServiceImpl;
import com.modsen.driverservice.service.impl.RideOrderServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.modsen.driverservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AcceptRideStepDefinitions {

    private DriverRepository driverRepository;
    private DriverMapper driverMapper;
    private DriverServiceImpl driverService;
    private RideOrderService rideOrderService;

    private AcceptRideMessage acceptRideMessage;
    private Driver driver;

    @Before
    public void setUp() {
        this.driverRepository = mock(DriverRepository.class);
        this.driverMapper = mock(DriverMapper.class);
        this.driverService = new DriverServiceImpl(driverRepository, driverMapper);
        this.rideOrderService = new RideOrderServiceImpl(driverService);
    }

    @Given("There are available drivers")
    public void thereAreAvailableDrivers() {
        var retrievedDrivers = List.of(getDefaultDriver());
        driver = getDefaultDriver();

        doReturn(retrievedDrivers)
                .when(driverRepository)
                .findAllByStatus(DriverStatus.AVAILABLE);
        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(List.of(getDefaultDriverResponse()))
                .when(driverMapper)
                .fromEntityListToResponseList(retrievedDrivers);

        var availableDrivers = driverService.getAvailableDrivers();

        assertThat(availableDrivers).isNotEmpty();
    }

    @Given("There are no available drivers")
    public void thereAreNoAvailableDrivers() {
        List<Driver> retrievedDrivers = Collections.emptyList();

        doReturn(retrievedDrivers)
                .when(driverRepository)
                .findAllByStatus(DriverStatus.AVAILABLE);
        doReturn(new ArrayList<DriverResponse>())
                .when(driverMapper)
                .fromEntityListToResponseList(retrievedDrivers);

        var availableDrivers = driverService.getAvailableDrivers();

        assertThat(availableDrivers).isEmpty();
    }

    @When("The create ride message with ride id {long} passed to acceptRideOrder method")
    public void createRideMessagePassedToAcceptRideOrderMethod(long id) {
        acceptRideMessage = rideOrderService.acceptRideOrder(new CreateRideMessage(id));
    }

    @Then("Accept ride message with assigned driver id should be returned")
    public void acceptRideMessageWithDriverIdReturned() {
        assertThat(acceptRideMessage.driverId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(driver.getStatus()).isEqualTo(DriverStatus.UNAVAILABLE);
    }

    @Then("Accept ride message without assigned driver id should be returned")
    public void acceptRideMessageWithoutDriverIdReturned() {
        assertThat(acceptRideMessage.driverId()).isNull();
    }
}
