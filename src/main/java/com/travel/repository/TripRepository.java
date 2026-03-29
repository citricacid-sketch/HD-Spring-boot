package com.travel.repository;

import com.travel.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(String userId);
    List<Trip> findByUserIdAndStatus(String userId, String status);
    Trip findByIdAndUserId(Long id, String userId);
    void deleteByIdAndUserId(Long id, String userId);
}
