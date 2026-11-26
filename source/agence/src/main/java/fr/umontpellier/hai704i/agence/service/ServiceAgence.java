package fr.umontpellier.hai704i.agence.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.umontpellier.hai704i.agence.domaine.Incident;
import fr.umontpellier.hai704i.agence.domaine.ResultatRecherche;
import org.springframework.stereotype.Service;

import fr.umontpellier.hai704i.agence.domaine.Offre;

/**
 * La logique de l'agence. Elle ne connaît que l'interface {@link Hotel} :
 * aucun import de Spring Web, aucune URL, aucun code de statut (A4.6).
 *
 * Elle interroge tous les hôtels configurés et rend un résultat partiel si certains
 * sont en panne (A6.3). La marge (A7) viendra ici.
 */
@Service
public class ServiceAgence {

    private final List<Hotel> hotels;


    public ServiceAgence(List<Hotel> hotels) {
        this.hotels = new ArrayList<>(hotels);
    }

    public ResultatRecherche rechercher(String ville, LocalDate arrivee, LocalDate depart, Integer personnes) {
        List<Offre> offres = new ArrayList<>();
        List<Incident> incidents = new ArrayList<>();
        for (Hotel hotel : hotels) {
            try {
                offres.addAll(hotel.chercher(ville, arrivee, depart, personnes));
            } catch (HotelIndisponible e) {
                // pas de throw : la panne d'un hôtel ne fait pas perdre les offres des autres
                incidents.add(new Incident(hotel.nom(), e.getNature(), e.getMessage()));
            }
        }
        if (!hotels.isEmpty() && incidents.size() == hotels.size()) {
            throw new AmontIndisponible(incidents);
        }
        return new ResultatRecherche(offres, incidents);
    }

    public Offre offre(String offerId) {
        // deux hôtels peuvent avoir le même identifiant : le choix de l'hôtel viendra en A7.3
        return hotels.get(0).offre(offerId);
    }
}
