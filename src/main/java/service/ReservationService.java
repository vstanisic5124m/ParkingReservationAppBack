package service;

import dto.ReservationRequest;
import dto.ReservationResponse;
import entity.*;
import repository.OwnerCancellationRepo;
import repository.ParkingSpaceRepo;
import repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class ReservationService {

    @Autowired
    private ReservationRepo reservationRepository;

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepository;

    @Autowired
    private OwnerCancellationRepo ownerCancellationRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, User user) {
        // Proveri i izvrsi validaciju da li parking mesto postoji
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(request.getParkingSpaceId())
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Proveri da li je mesto otkazano od strane korisnika
        Optional<OwnerCancellation> cancellation = ownerCancellationRepository
                .findByParkingSpaceIdAndCancellationDate(request.getParkingSpaceId(), request.getReservationDate());
        if (cancellation.isPresent()) {
            throw new RuntimeException("Parking space is not available on this date");
        }

        // Proveri da li je vec rezervisano
        Optional<Reservation> existing = reservationRepository
                .findByParkingSpaceIdAndReservationDateAndStatus(
                        request.getParkingSpaceId(),
                        request.getReservationDate(),
                        ReservationStatus.ACTIVE
                );
        if (existing.isPresent()) {
            throw new RuntimeException("Parking space is already reserved for this date");
        }

        // Kreiraj rezervaciju
        Reservation reservation = Reservation.builder()
                .user(user)
                .parkingSpace(parkingSpace)
                .reservationDate(request.getReservationDate())
                .status(ReservationStatus.ACTIVE)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        // Posalji email korisniku i daj mu feedback u vidu notifikacije
        emailService.sendReservationConfirmation(user, saved);

        return ReservationResponse.builder()
                .id(saved.getId())
                .parkingSpaceId(saved.getParkingSpace().getId())
                .parkingType(saved.getParkingSpace().getParkingType().name())
                .spotNumber(saved.getParkingSpace().getSpotNumber())
                .reservationDate(saved.getReservationDate())
                .status(saved.getStatus().name())
                .build();
    }

    @Transactional
    public void cancelReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Verifikuj da korisnik zaista poseduje ovu rezervaciju
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own reservations");
        }

        // Update obavezno
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Posalji mejl korisniku u vidu notifikacije
        emailService.sendReservationCancellation(user, reservation);
    }
    public java.util.List<ReservationResponse> getMyReservations(User user) {
        java.util.List<Reservation> reservations = reservationRepository
                .findByUserIdAndStatus(user.getId(), ReservationStatus.ACTIVE);

        return reservations.stream()
                .map(r -> ReservationResponse.builder()
                        .id(r.getId())
                        .parkingSpaceId(r.getParkingSpace().getId())
                        .parkingType(r.getParkingSpace().getParkingType().name())
                        .spotNumber(r.getParkingSpace().getSpotNumber())
                        .reservationDate(r.getReservationDate())
                        .status(r.getStatus().name())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
}
