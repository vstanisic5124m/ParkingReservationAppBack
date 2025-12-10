package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCancellationRequest {
    @NotNull(message = "Cancellation date is required")
    private LocalDate cancellationDate;
}