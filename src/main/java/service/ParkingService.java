package service;

import dto.ParkingSpaceDto;
import entity.*;
import repository.OwnerCancellationRepo;
import repository.ParkingSpaceRepo;
import repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepository;

    @Autowired
    private ReservationRepo reservationRepository;

    @Autowired
    private OwnerCancellationRepo ownerCancellationRepository;

    public List<ParkingSpaceDto> getParkingAvailability(LocalDate date, Long currentUserId) {
        List<ParkingSpace> allSpaces = parkingSpaceRepository.findByIsActiveTrue();

        // Get za sve aktivne rezervacije na dan
        List<Reservation> reservations = reservationRepository.findByReservationDateAndStatus(
                date, ReservationStatus.ACTIVE
        );

        Map<Long, Reservation> reservationMap = reservations.stream()
                .collect(Collectors.toMap(r -> r.getParkingSpace().getId(), r -> r));

        // Get sva otkazivanja parkinga vlasnika na dan
        List<OwnerCancellation> cancellations = ownerCancellationRepository.findByCancellationDate(date);
        Map<Long, OwnerCancellation> cancellationMap = cancellations.stream()
                .collect(Collectors.toMap(c -> c.getParkingSpace().getId(), c -> c));

        List<ParkingSpaceDto> result = new ArrayList<>();

        for (ParkingSpace space : allSpaces) {
            String status;

            if (cancellationMap.containsKey(space.getId())) {
                status = "owner-cancelled";
            } else if (reservationMap.containsKey(space.getId())) {
                Reservation reservation = reservationMap.get(space.getId());
                if (reservation.getUser().getId().equals(currentUserId)) {
                    status = "my-reservation";
                } else {
                    status = "occupied";
                }
            } else {
                status = "available";
            }

            result.add(ParkingSpaceDto.builder()
                    .id(space.getId())
                    .parkingType(space.getParkingType())
                    .spotNumber(space.getSpotNumber())
                    .status(status)
                    .build());
        }

        return result;
    }
}
