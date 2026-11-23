package fr.umontpellier.hai704i.hotel.service;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.depot.ReservationDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import fr.umontpellier.hai704i.hotel.domaine.State;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ServiceReservation {

    private final ReservationDepot reservationDepot;
    private final OffreDepot offreDepot;
    private AtomicInteger idGenerator = new AtomicInteger(1001);

    public ServiceReservation(ReservationDepot reservationDepot, OffreDepot offreDepot) {
        this.reservationDepot = reservationDepot;
        this.offreDepot = offreDepot;
    }

    public Reservation creerReservation(String offerId,
                                        int nbPersons,
                                        String firstName,
                                        String lastName) {

        Optional<Offre> offre = offreDepot.parId(offerId);
        if(offre.isEmpty()) throw new Introuvable("L'offre n'existe pas");
        if(!offre.get().available()) throw new Conflit("L'offre n'est pas disponible");
        if(offre.get().nbPersons() < nbPersons) throw new Conflit("Il n'y a pas assez de places disponibles");

        Offre resa = offre.get().reservee();
        offreDepot.enregistrer(resa);
        return reservationDepot.enregistrer(new Reservation("B-"+idGenerator.getAndIncrement(), resa.id(), resa.price(), nbPersons, firstName, lastName, State.CONFIRMED));
    }

    public Reservation lireReservation(String id){
        Optional<Reservation> resa = reservationDepot.parId(id);
        if(resa.isPresent()){
            return resa.get();
        }
        throw new Introuvable("La reservation d'id "+ id + " n'existe pas");
    }
}