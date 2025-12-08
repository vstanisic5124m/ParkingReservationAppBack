package repository;

import entity.ParkingSpace;
import entity.ParkingType;
import entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpaceRepo extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByIsActiveTrue();
    List<ParkingSpace> findByParkingTypeAndIsActiveTrue(ParkingType parkingType);
}
