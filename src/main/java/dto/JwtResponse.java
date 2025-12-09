package dto;

import entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private Long ownedParkingSpaceId;
    private Boolean isOwner;
    private Integer rating;
}
