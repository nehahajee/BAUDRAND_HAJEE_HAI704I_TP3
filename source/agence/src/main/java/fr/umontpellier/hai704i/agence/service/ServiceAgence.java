package fr.umontpellier.hai704i.agence.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.umontpellier.hai704i.agence.domaine.Offre;

/**
 * La logique de l'agence. Elle ne connaît que l'interface {@link Hotel} :
 * aucun import de Spring Web, aucune URL, aucun code de statut (A4.6).
 *
 * Elle interroge tous les hôtels configurés; la gestion des pannes (A6.3)
 * et la marge (A7) viendront ici.
 */
@Service
public class ServiceAgence {

    private final List<Hotel> hotels;

    public ServiceAgence(List<Hotel> hotels) {
        this.hotels = new ArrayList<>(hotels);
    }

    public List<Offre> rechercher(String ville, LocalDate arrivee, LocalDate depart, Integer personnes) {
        List<Offre> offres = new ArrayList<>();
        for (Hotel hotel : hotels) {
            offres.addAll(hotel.chercher(ville, arrivee, depart, personnes));
        }
        return offres;
    }

    public Offre offre(String offerId) {
        // deux hôtels peuvent avoir le même identifiant : le choix de l'hôtel viendra en A7.3
        return hotels.get(0).offre(offerId);
    }
}
