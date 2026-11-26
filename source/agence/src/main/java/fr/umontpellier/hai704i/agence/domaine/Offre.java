package fr.umontpellier.hai704i.agence.domaine;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Une offre telle que le métier de l'agence la manipule.
 * Elle ne dépend pas du format JSON de l'hôtel : c'est le client qui fait la conversion.
 */
public record Offre(String id,
                    String hotelName,
                    String city,
                    BigDecimal price,
                    int nbPersons,
                    int nbBeds,
                    boolean breakfast,
                    LocalDate arrivalDate,
                    LocalDate departureDate) {
}
