package dto;


import entity.ParkingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaceDto {
    private Long id;
    private ParkingType parkingType;
    private Integer spotNumber;
    private String status;  // "available", "occupied", "my-reservation", "owner-cancelled"
}
