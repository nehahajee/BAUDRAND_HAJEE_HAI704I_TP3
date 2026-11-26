package fr.umontpellier.hai704i.hotel.service;

public class NonAuthentifie extends RuntimeException {
    public NonAuthentifie(String message) {
        super(message);
    }
}
