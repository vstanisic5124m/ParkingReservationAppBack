package com.parkingshare.auth.service;

import com.parkingshare.auth.dto.OwnerCancellationRequest;
import  com.parkingshare.auth.entity.OwnerCancellation;
import  com.parkingshare.auth.entity.ParkingSpace;
import  com.parkingshare.auth.entity.User;
import  com.parkingshare.auth.repository.OwnerCancellationRepo;
import  com.parkingshare.auth.repository.ParkingSpaceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class OwnerService {

    @Autowired
    private OwnerCancellationRepo ownerCancellationRepository;

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void cancelSpotAvailability(OwnerCancellationRequest request, User owner) {
        // Validiraj vlasnika parking mesta
        if (owner.getOwnedParkingSpaceId() == null) {
            throw new RuntimeException("You do not own a parking space");
        }

        // Validiraj da li to parking mesto postoji
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(owner.getOwnedParkingSpaceId())
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        LocalDate cancellationDate = request.getCancellationDate();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // 17h pravilo po specifikaciji ---> dakle ne moze da rezervise ako je proslo 17h
        LocalDate tomorrow = today.plusDays(1);
        if (cancellationDate.equals(tomorrow) && currentTime.isAfter(LocalTime.of(17, 0))) {
            throw new RuntimeException("Cannot cancel for tomorrow after 5:00 PM");
        }

        // ne moze da otkaze za danas
        if (cancellationDate.equals(today)) {
            throw new RuntimeException("Cannot cancel availability for today");
        }

        // Ne moze da otkaze u proslosti
        if (cancellationDate.isBefore(today)) {
            throw new RuntimeException("Cannot cancel availability for past dates");
        }

        // Proveri da li je vec otkazano
        if (ownerCancellationRepository.findByParkingSpaceIdAndCancellationDate(
                parkingSpace.getId(), cancellationDate).isPresent()) {
            throw new RuntimeException("Availability already cancelled for this date");
        }

        // Kreiraj otkazivanje
        OwnerCancellation cancellation = OwnerCancellation.builder()
                .parkingSpace(parkingSpace)
                .cancellationDate(cancellationDate)
                .build();

        ownerCancellationRepository.save(cancellation);

        // Posalji korisniku email notifikaciju i obavesti ga (daj mu feedback)
        emailService.sendOwnerCancellationNotification(owner, parkingSpace, cancellationDate);
    }
}
