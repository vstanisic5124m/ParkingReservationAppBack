package controller;

import dto.ReservationRequest;
import dto.ReservationResponse;
import entity.User;
import service.ReservationService;

import util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest request) {
        try {
            User currentUser = securityUtil.getCurrentUser();
            ReservationResponse response = reservationService.createReservation(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            User currentUser = securityUtil.getCurrentUser();
            reservationService.cancelReservation(id, currentUser);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reservation cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        User currentUser = securityUtil.getCurrentUser();
        List<ReservationResponse> reservations = reservationService.getMyReservations(currentUser);
        return ResponseEntity.ok(reservations);
    }
}
}
