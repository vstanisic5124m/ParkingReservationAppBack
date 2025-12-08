package entity;

public enum UserRole {
    NON_OWNER,  // Standardni korisnik koji moze samo da rezervise parking mesto
    OWNER,      // Korisnik koji poseduje parking mesto i moze isto da otkaze
    ADMIN       // Administrator koji upravlja korisnicima i njihovim ulogama
}
