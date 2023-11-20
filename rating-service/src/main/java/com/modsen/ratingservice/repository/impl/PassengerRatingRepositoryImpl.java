package com.modsen.ratingservice.repository.impl;

import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.modsen.ratingservice.util.Queries.*;

@Repository
public class PassengerRatingRepositoryImpl implements PassengerRatingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(PassengerRating rating) {
        entityManager.persist(rating);
    }

    @Override
    public BigDecimal findPassengerRating(long passengerId) {
        Query query = entityManager.createQuery(FIND_PASSENGER_RATING_QUERY)
                .setParameter("passengerId", passengerId);
        return BigDecimal.valueOf((Double) query.getSingleResult());
    }
}
