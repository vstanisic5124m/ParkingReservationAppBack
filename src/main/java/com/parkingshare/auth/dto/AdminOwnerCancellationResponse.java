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
public class AdminOwnerCancellationResponse {
    private Long id;
    private Long parkingSpaceId;
    private String parkingType;
    private Integer spotNumber;
    private LocalDate cancellationDate;
    private LocalDateTime createdAt;
}
