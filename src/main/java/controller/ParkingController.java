package controller;

import dto.ParkingSpaceDto;
import entity.User;
import service.ParkingService;
import util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/spaces")
    public ResponseEntity<List<ParkingSpaceDto>> getParkingSpaces(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User currentUser = securityUtil.getCurrentUser();
        List<ParkingSpaceDto> spaces = parkingService.getParkingAvailability(date, currentUser.getId());
        return ResponseEntity.ok(spaces);
    }
}
