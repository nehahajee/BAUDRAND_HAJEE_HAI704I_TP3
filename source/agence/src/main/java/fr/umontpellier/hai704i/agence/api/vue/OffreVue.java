package fr.umontpellier.hai704i.agence.api.vue;

import java.math.BigDecimal;
import java.time.LocalDate;

import fr.umontpellier.hai704i.agence.domaine.Offre;

/**
 * La représentation d'une offre publiée par l'agence : ce qui circule vers le comparateur.
 * La marge et les identifiants préfixés par l'hôtel viendront en A7.
 */
public record OffreVue(String id,
                       String hotelName,
                       String city,
                       BigDecimal price,
                       int nbPersons,
                       int nbBeds,
                       boolean breakfast,
                       LocalDate arrivalDate,
                       LocalDate departureDate) {

    public static OffreVue depuis(Offre o) {
        return new OffreVue(o.id(), o.hotelName(), o.city(), o.price(), o.nbPersons(),
                o.nbBeds(), o.breakfast(), o.arrivalDate(), o.departureDate());
    }
}
