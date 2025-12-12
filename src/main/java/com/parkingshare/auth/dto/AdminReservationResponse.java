package com.parkingshare.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReservationResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private Long parkingSpaceId;
    private String parkingType;
    private Integer spotNumber;
    private LocalDate reservationDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
