package com.parkingshare.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatisticsResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Long ownersCount;
    private Long totalParkingSpaces;
    private Long activeParkingSpaces;
    private Long totalReservations;
    private Long activeReservations;
    private Long cancelledReservations;
    private Long totalOwnerCancellations;
}
