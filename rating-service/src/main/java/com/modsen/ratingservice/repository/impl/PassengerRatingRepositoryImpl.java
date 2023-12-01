package com.modsen.ratingservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.modsen.ratingservice.util.Queries.*;

@Component
public class PassengerRatingRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public BigDecimal findPassengerRating(long passengerId) {
        Query query = entityManager.createQuery(FIND_PASSENGER_RATING_QUERY)
                .setParameter("passengerId", passengerId);
        return BigDecimal.valueOf((Double) query.getSingleResult());
    }
}
