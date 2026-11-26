package fr.umontpellier.hai704i.hotel.service;

public class PreconditionManquante extends RuntimeException {
    public PreconditionManquante(String message) {
        super(message);
    }
}
