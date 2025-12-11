package com.parkingshare.auth.entity;

public enum ReservationStatus {
    ACTIVE,      // Trenutno aktivno mesto
    CANCELLED,   // Korisnik je otkazao svoju rezervaciju
    COMPLETED    // Rezervacija je uspesno prosla i izvrsena
}
