package repository;

import entity.OwnerCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerCancellationRepo extends JpaRepository<OwnerCancellation, Long> {
    List<OwnerCancellation> findByCancellationDate(LocalDate date);
    Optional<OwnerCancellation> findByParkingSpaceIdAndCancellationDate(Long parkingSpaceId, LocalDate date);
}
