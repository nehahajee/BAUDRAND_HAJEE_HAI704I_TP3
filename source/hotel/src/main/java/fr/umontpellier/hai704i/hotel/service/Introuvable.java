package fr.umontpellier.hai704i.hotel.service;


public class Introuvable extends RuntimeException {

    public Introuvable(String message) {
        super(message);
    }
}
