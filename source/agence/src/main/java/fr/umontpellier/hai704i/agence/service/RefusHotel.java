package fr.umontpellier.hai704i.agence.service;

public class RefusHotel extends RuntimeException {
    public RefusHotel(String message) {
        super(message);
    }
}
