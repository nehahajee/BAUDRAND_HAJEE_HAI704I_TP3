package fr.umontpellier.hai704i.hotel.service;


public class Conflit extends RuntimeException {
    public Conflit(String message) {
        super(message);
    }
}
