package com.travel.repository;

import com.travel.entity.TripSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripSlotRepository extends JpaRepository<TripSlot, Long> {
    List<TripSlot> findByTripDayId(Long tripDayId);
    void deleteByTripDayId(Long tripDayId);
}
