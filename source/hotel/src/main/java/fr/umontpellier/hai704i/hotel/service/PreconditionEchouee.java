package fr.umontpellier.hai704i.hotel.service;

/**
 * Le client a posé une condition sur l'état de la ressource ({@code If-Match})
 * et cette condition est fausse.
 * Se traduit par un 412.
 */
public class PreconditionEchouee extends RuntimeException {
    public PreconditionEchouee(String message) {
        super(message);
    }
}
