package com.parkingshare.auth.service;

import com.parkingshare.auth.dto.*;
import com.parkingshare.auth.entity.OwnerCancellation;
import com.parkingshare.auth.entity.ParkingSpace;
import com.parkingshare.auth.entity.Reservation;
import com.parkingshare.auth.entity.ReservationStatus;
import com.parkingshare.auth.repository.OwnerCancellationRepo;
import com.parkingshare.auth.repository.ParkingSpaceRepo;
import com.parkingshare.auth.repository.ReservationRepo;
import com.parkingshare.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepository;

    @Autowired
    private ReservationRepo reservationRepository;

    @Autowired
    private OwnerCancellationRepo ownerCancellationRepository;

    public AdminStatisticsResponse getStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(user -> user.getIsActive())
                .count();
        long ownersCount = userRepository.findAll().stream()
                .filter(user -> user.getIsOwner())
                .count();

        long totalParkingSpaces = parkingSpaceRepository.count();
        long activeParkingSpaces = parkingSpaceRepository.findByIsActiveTrue().size();

        long totalReservations = reservationRepository.count();
        long activeReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .count();
        long cancelledReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED)
                .count();

        long totalOwnerCancellations = ownerCancellationRepository.count();

        return AdminStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .ownersCount(ownersCount)
                .totalParkingSpaces(totalParkingSpaces)
                .activeParkingSpaces(activeParkingSpaces)
                .totalReservations(totalReservations)
                .activeReservations(activeReservations)
                .cancelledReservations(cancelledReservations)
                .totalOwnerCancellations(totalOwnerCancellations)
                .build();
    }

    public List<AdminReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(r -> AdminReservationResponse.builder()
                        .id(r.getId())
                        .userId(r.getUser().getId())
                        .userEmail(r.getUser().getEmail())
                        .userName(r.getUser().getFirstName() + " " + r.getUser().getLastName())
                        .parkingSpaceId(r.getParkingSpace().getId())
                        .parkingType(r.getParkingSpace().getParkingType().name())
                        .spotNumber(r.getParkingSpace().getSpotNumber())
                        .reservationDate(r.getReservationDate())
                        .status(r.getStatus().name())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AdminReservationResponse> getReservationsByStatus(String status) {
        ReservationStatus reservationStatus;
        try {
            reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }

        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == reservationStatus)
                .collect(Collectors.toList());

        return reservations.stream()
                .map(r -> AdminReservationResponse.builder()
                        .id(r.getId())
                        .userId(r.getUser().getId())
                        .userEmail(r.getUser().getEmail())
                        .userName(r.getUser().getFirstName() + " " + r.getUser().getLastName())
                        .parkingSpaceId(r.getParkingSpace().getId())
                        .parkingType(r.getParkingSpace().getParkingType().name())
                        .spotNumber(r.getParkingSpace().getSpotNumber())
                        .reservationDate(r.getReservationDate())
                        .status(r.getStatus().name())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AdminParkingSpaceResponse> getAllParkingSpaces() {
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();

        return parkingSpaces.stream()
                .map(ps -> AdminParkingSpaceResponse.builder()
                        .id(ps.getId())
                        .parkingType(ps.getParkingType().name())
                        .spotNumber(ps.getSpotNumber())
                        .isActive(ps.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AdminOwnerCancellationResponse> getAllOwnerCancellations() {
        List<OwnerCancellation> cancellations = ownerCancellationRepository.findAll();

        return cancellations.stream()
                .map(oc -> AdminOwnerCancellationResponse.builder()
                        .id(oc.getId())
                        .parkingSpaceId(oc.getParkingSpace().getId())
                        .parkingType(oc.getParkingSpace().getParkingType().name())
                        .spotNumber(oc.getParkingSpace().getSpotNumber())
                        .cancellationDate(oc.getCancellationDate())
                        .createdAt(oc.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
