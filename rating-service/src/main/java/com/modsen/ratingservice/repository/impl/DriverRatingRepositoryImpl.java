package com.modsen.ratingservice.repository.impl;

import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.modsen.ratingservice.util.Queries.*;

@Repository
public class DriverRatingRepositoryImpl implements DriverRatingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(DriverRating rating) {
        entityManager.persist(rating);
    }

    @Override
    public BigDecimal findDriverRating(long driverId) {
        Query query = entityManager.createQuery(FIND_DRIVER_RATING_QUERY)
                .setParameter("driverId", driverId);
        return BigDecimal.valueOf((Double) query.getSingleResult());
    }
}
