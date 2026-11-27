package fr.umontpellier.hai704i.hotel.service;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.depot.ReservationDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import fr.umontpellier.hai704i.hotel.domaine.State;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceReservation {

    private final ReservationDepot reservationDepot;
    private final OffreDepot offreDepot;

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
            if(offerId.equals(resa.get().offerId())){
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
        Reservation cree = reservationDepot.enregistrer((new Reservation(reservationDepot.prochainId(),
                offreResa.id(), offreResa.price(), nbPersons, firstName, lastName,
                State.CONFIRMED, agence, cleIdempotence, 1)));
        return new Creation(cree, false);
    }

    public Reservation lireReservation(String id, String agence){
        Optional<Reservation> resa = reservationDepot.parId(id);
        if(resa.isPresent()){
            if(!agence.equals(resa.get().agency())) throw new AccesInterdit("Une agence essaye d'accéder à une reservation d'une autre");
            return resa.get();
        }
        throw new Introuvable("La reservation d'id "+ id + " n'existe pas");
    }

    private void verifierVersion(Reservation actuel, String versionAttendue) {
        if (versionAttendue == null || versionAttendue.isBlank()) {
            throw new PreconditionManquante("en-tête If-Match attendu");
        }
        String attendu = versionAttendue.replace("\"", "").replace("W/", "").trim();
        if (!attendu.equals(String.valueOf(actuel.version())) && !attendu.equals("*")) {
            throw new PreconditionEchouee("la réservation a changé depuis la version " + attendu);
        }
    }

    public synchronized Reservation modifier(String id, String firstName, String lastName, Integer nbPersons ,String versionAttendue, String agence) {
        Reservation actuelle = lireReservation(id, agence);
        verifierVersion(actuelle, versionAttendue);

        Optional<Offre> offre = offreDepot.parId(actuelle.offerId());
        if(actuelle.state() == State.CANCELED) throw new Conflit("La réservation est annulée");
        if(nbPersons!=null) if(offre.get().nbPersons() < nbPersons) throw new Conflit("Il n'y a pas assez de places disponibles");

        if(firstName==null && lastName==null && nbPersons==null ) return actuelle;

        return reservationDepot.enregistrer(actuelle.avec(firstName, lastName, nbPersons));
    }

    public synchronized Reservation annuler(String id, String agence) {
        Reservation actuelle = lireReservation(id, agence);

        if(actuelle.state() == State.CANCELED) return actuelle;

        Optional<Offre> offre = offreDepot.parId(actuelle.offerId());

        Offre offreResa = offre.get().liberee();
        offreDepot.enregistrer(offreResa);

        return reservationDepot.enregistrer(actuelle.annulee());

    }

    public record Creation(Reservation reservation, boolean rejeu) { }
}