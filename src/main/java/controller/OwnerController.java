package controller;

import dto.OwnerCancellationRequest;
import entity.User;
import entity.UserRole;
import service.OwnerService;
import util.SecurityUtil;
import jakarta.validation.Valid;import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSpotAvailability(@Valid @RequestBody OwnerCancellationRequest request) {
        try {
            User currentUser = securityUtil.getCurrentUser();

            // Verify user is an owner
            if (currentUser.getRole() != UserRole.OWNER) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Only parking space owners can cancel availability");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            ownerService.cancelSpotAvailability(request, currentUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Parking spot availability cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}