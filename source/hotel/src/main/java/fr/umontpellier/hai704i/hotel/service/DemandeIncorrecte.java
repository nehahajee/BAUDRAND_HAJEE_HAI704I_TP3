package fr.umontpellier.hai704i.hotel.service;

public class DemandeIncorrecte extends RuntimeException {
    public DemandeIncorrecte(String message) {
        super(message);
    }
}
