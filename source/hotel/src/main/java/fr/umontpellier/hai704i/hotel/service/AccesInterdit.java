package fr.umontpellier.hai704i.hotel.service;

public class AccesInterdit extends RuntimeException {
    public AccesInterdit(String message) {
        super(message);
    }
}
