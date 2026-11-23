package fr.umontpellier.hai704i.hotel.api.vue;

import java.math.BigDecimal;
import java.time.LocalDate;

import fr.umontpellier.hai704i.hotel.domaine.Offre;

public record OffreVue(String id,
                       BigDecimal price,
                       int nbPersons,
                       String city,
                       int nbBeds,
                       boolean breakfast,
                       LocalDate arrivalDate,
                       LocalDate departureDate,
                       boolean available,
                       String hotelName,
                       String pictureLink) {


    public static OffreVue depuis(Offre o, String base, String hotelName) {
        String lien = (o.pictureFile() == null) ? null
                : base + "/api/v1/offers/" + o.id() + "/picture";
        return new OffreVue(o.id(), o.price(), o.nbPersons(), o.city(), o.nbBeds(),
                o.breakfast(), o.arrivalDate(), o.departureDate(), o.available(),
                hotelName, lien);
    }
}
