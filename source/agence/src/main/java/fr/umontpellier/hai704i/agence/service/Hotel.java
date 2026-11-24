package fr.umontpellier.hai704i.agence.service;

import java.time.LocalDate;
import java.util.List;

import fr.umontpellier.hai704i.agence.domaine.Offre;

/**
 * Ce que l'agence attend d'un hôtel, sans dire comment on le joint.
 * La réalisation HTTP est {@code client.ClientHotel} ; le service ne connaît que cette interface.
 *
 * <p>Chaque méthode peut lever {@link RefusHotel} (l'hôtel a répondu non)
 * ou {@link HotelIndisponible} (l'hôtel est en panne, lent ou injoignable).
 */
public interface Hotel {

    List<Offre> chercher(String ville, LocalDate arrivee, LocalDate depart, Integer personnes);

    Offre offre(String offerId);
}
