package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_spaces")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParkingType parkingType;

    @Column(nullable = false)
    private Integer spotNumber;  // 1-50 for YARD, 1-100 for GARAGE

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
