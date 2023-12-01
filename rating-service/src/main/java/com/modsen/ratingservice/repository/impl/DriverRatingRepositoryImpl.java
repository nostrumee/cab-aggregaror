package com.modsen.ratingservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.modsen.ratingservice.util.Queries.*;

@Component
public class DriverRatingRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public BigDecimal findDriverRating(long driverId) {
        Query query = entityManager.createQuery(FIND_DRIVER_RATING_QUERY)
                .setParameter("driverId", driverId);
        return BigDecimal.valueOf((Double) query.getSingleResult());
    }
}
