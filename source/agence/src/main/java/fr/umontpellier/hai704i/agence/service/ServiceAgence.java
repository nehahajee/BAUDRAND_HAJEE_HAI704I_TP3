package fr.umontpellier.hai704i.agence.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.umontpellier.hai704i.agence.domaine.Offre;

/**
 * La logique de l'agence. Elle ne connaît que l'interface {@link Hotel} :
 * aucun import de Spring Web, aucune URL, aucun code de statut (A4.6).
 *
 * <p>Pour l'instant elle délègue à un seul hôtel ; plusieurs hôtels (A6)
 * et la marge (A7) viendront ici.
 */
@Service
public class ServiceAgence {

    private final Hotel hotel;

    public ServiceAgence(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Offre> rechercher(String ville, LocalDate arrivee, LocalDate depart, Integer personnes) {
        return hotel.chercher(ville, arrivee, depart, personnes);
    }

    public Offre offre(String offerId) {
        return hotel.offre(offerId);
    }
}
