package controller;

import dto.UpdateOwnerStatusRequest;
import dto.UpdateRatingRequest;
import dto.UserListResponse;
import entity.*;
import repository.UserRepository;
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
}