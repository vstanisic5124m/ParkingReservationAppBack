package com.parkingshare.auth.repository;

import com.parkingshare.auth.entity.Reservation;
import com.parkingshare.auth.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReservationDateAndStatus(LocalDate date, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.reservationDate = :date AND r.status = :status")
    Optional<Reservation> findByUserIdAndDateAndStatus(@Param("userId") Long userId,
                                                       @Param("date") LocalDate date,
                                                       @Param("status") ReservationStatus status);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    Optional<Reservation> findByParkingSpaceIdAndReservationDateAndStatus(Long parkingSpaceId,
                                                                          LocalDate date,
                                                                          ReservationStatus status);
}
