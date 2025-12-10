package dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    @NotNull(message = "Parking space ID is required")
    private Long parkingSpaceId;
    @NotNull(message = "Reservation date is required")
    private LocalDate reservationDate;
}
