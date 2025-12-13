package com.parkingshare.auth.service;

import com.parkingshare.auth.dto.*;
import com.parkingshare.auth.entity.Admin;
import com.parkingshare.auth.entity.OwnerCancellation;
import com.parkingshare.auth.entity.ParkingSpace;
import com.parkingshare.auth.entity.Reservation;
import com.parkingshare.auth.entity.ReservationStatus;
import com.parkingshare.auth.repository.AdminRepository;
import com.parkingshare.auth.repository.OwnerCancellationRepo;
import com.parkingshare.auth.repository.ParkingSpaceRepo;
import com.parkingshare.auth.repository.ReservationRepo;
import com.parkingshare.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepository;

    @Autowired
    private ReservationRepo reservationRepository;

    @Autowired
    private OwnerCancellationRepo ownerCancellationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminStatisticsResponse getStatistics() {
        // Fetch all users once and compute statistics
        List<com.parkingshare.auth.entity.User> allUsers = userRepository.findAll();
        long totalUsers = allUsers.size();
        long activeUsers = allUsers.stream()
                .filter(user -> user.getIsActive())
                .count();
        long ownersCount = allUsers.stream()
                .filter(user -> user.getIsOwner())
                .count();

        long totalParkingSpaces = parkingSpaceRepository.count();
        long activeParkingSpaces = parkingSpaceRepository.findByIsActiveTrue().size();

        // Fetch all reservations once and compute statistics
        List<Reservation> allReservations = reservationRepository.findAll();
        long totalReservations = allReservations.size();
        long activeReservations = allReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .count();
        long cancelledReservations = allReservations.stream()
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
        return mapToAdminReservationResponses(reservations);
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

        return mapToAdminReservationResponses(reservations);
    }

    private List<AdminReservationResponse> mapToAdminReservationResponses(List<Reservation> reservations) {
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

    // Admin entity CRUD operations
    public AdminResponse createAdmin(CreateAdminRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Admin with this email already exists");
        }

        Admin admin = Admin.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        return mapToAdminResponse(savedAdmin);
    }

    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToAdminResponse)
                .collect(Collectors.toList());
    }

    public AdminResponse getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
        return mapToAdminResponse(admin);
    }

    public AdminResponse updateAdmin(Long id, UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        if (request.getFirstName() != null) {
            admin.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            admin.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            admin.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getIsActive() != null) {
            admin.setIsActive(request.getIsActive());
        }

        Admin updatedAdmin = adminRepository.save(admin);
        return mapToAdminResponse(updatedAdmin);
    }

    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    private AdminResponse mapToAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phoneNumber(admin.getPhoneNumber())
                .isActive(admin.getIsActive())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
