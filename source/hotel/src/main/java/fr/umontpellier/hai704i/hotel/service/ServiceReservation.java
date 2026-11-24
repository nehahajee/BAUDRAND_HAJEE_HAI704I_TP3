package fr.umontpellier.hai704i.hotel.service;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.depot.ReservationDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import fr.umontpellier.hai704i.hotel.domaine.State;
import org.springframework.stereotype.Service;
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


    public synchronized Creation creerReservation(String offerId,
                                        int nbPersons,
                                        String firstName,
                                        String lastName, String cleIdempotence, String agence) {

        if (cleIdempotence == null || cleIdempotence.isBlank()){
            throw new DemandeIncorrecte("Cle d'idempotence nulle ou vide");
        }

        Optional<Reservation> resa = reservationDepot.parCle(agence, cleIdempotence);
        if(resa.isPresent()){
            if(offerId.equals(resa.get().offerId()) && nbPersons == resa.get().nbPersons() && firstName.equals(resa.get().firstName()) && lastName.equals(resa.get().lastName())){
                return new Creation(resa.get(), true);
            }
            throw new Conflit("Meme cle d'idempotence pour deux reservation différentes");
        }

        Optional<Offre> offre = offreDepot.parId(offerId);
        if(offre.isEmpty()) throw new Introuvable("L'offre n'existe pas");
        if(!offre.get().available()) throw new Conflit("L'offre n'est pas disponible");
        if(offre.get().nbPersons() < nbPersons) throw new Conflit("Il n'y a pas assez de places disponibles");

        Offre offreResa = offre.get().reservee();
        offreDepot.enregistrer(offreResa);
        Reservation cree = reservationDepot.enregistrer((new Reservation("B-"+idGenerator.getAndIncrement(), offreResa.id(), offreResa.price(), nbPersons, firstName, lastName, State.CONFIRMED, agence, cleIdempotence)));
        return new Creation(cree, false);
    }

    public Reservation lireReservation(String id){
        Optional<Reservation> resa = reservationDepot.parId(id);
        if(resa.isPresent()){
            return resa.get();
        }
        throw new Introuvable("La reservation d'id "+ id + " n'existe pas");
    }

    public record Creation(Reservation reservation, boolean rejeu) { }
}