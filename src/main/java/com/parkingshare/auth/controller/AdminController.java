package com.parkingshare.auth.controller;

import com.parkingshare.auth.dto.*;
import com.parkingshare.auth.entity.*;
import com.parkingshare.auth.repository.UserRepository;
import com.parkingshare.auth.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> listAllUsers() {
        try {
            List<User> users = userRepository.findAll();

            List<UserListResponse> userList = users.stream()
                    .map(user -> UserListResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .phoneNumber(user.getPhoneNumber())
                            .isActive(user.getIsActive())
                            .isOwner(user.getIsOwner())
                            .rating(user.getRating())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/users/{userId}/owner-status")
    public ResponseEntity<?> updateOwnerStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateOwnerStatusRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setIsOwner(request.getIsOwner());
            User updatedUser = userRepository.save(user);

            UserListResponse response = UserListResponse.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .phoneNumber(updatedUser.getPhoneNumber())
                    .isActive(updatedUser.getIsActive())
                    .isOwner(updatedUser.getIsOwner())
                    .rating(updatedUser.getRating())
                    .createdAt(updatedUser.getCreatedAt())
                    .updatedAt(updatedUser.getUpdatedAt())
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update owner status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/users/{userId}/rating")
    public ResponseEntity<?> updateRating(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRatingRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setRating(request.getRating());
            User updatedUser = userRepository.save(user);

            UserListResponse response = UserListResponse.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .phoneNumber(updatedUser.getPhoneNumber())
                    .isActive(updatedUser.getIsActive())
                    .isOwner(updatedUser.getIsOwner())
                    .rating(updatedUser.getRating())
                    .createdAt(updatedUser.getCreatedAt())
                    .updatedAt(updatedUser.getUpdatedAt())
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update rating: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        try {
            AdminStatisticsResponse statistics = adminService.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getAllReservations(@RequestParam(required = false) String status) {
        try {
            List<AdminReservationResponse> reservations;
            if (status != null && !status.isEmpty()) {
                reservations = adminService.getReservationsByStatus(status);
            } else {
                reservations = adminService.getAllReservations();
            }
            return ResponseEntity.ok(reservations);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve reservations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/parking-spaces")
    public ResponseEntity<?> getAllParkingSpaces() {
        try {
            List<AdminParkingSpaceResponse> parkingSpaces = adminService.getAllParkingSpaces();
            return ResponseEntity.ok(parkingSpaces);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve parking spaces: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/owner-cancellations")
    public ResponseEntity<?> getAllOwnerCancellations() {
        try {
            List<AdminOwnerCancellationResponse> cancellations = adminService.getAllOwnerCancellations();
            return ResponseEntity.ok(cancellations);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve owner cancellations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Admin entity CRUD endpoints
    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        try {
            AdminResponse admin = adminService.createAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(admin);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to create admin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<AdminResponse> admins = adminService.getAllAdmins();
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve admins: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/admins/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        try {
            AdminResponse admin = adminService.getAdminById(id);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to retrieve admin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdminRequest request) {
        try {
            AdminResponse admin = adminService.updateAdmin(id, request);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update admin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to delete admin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}