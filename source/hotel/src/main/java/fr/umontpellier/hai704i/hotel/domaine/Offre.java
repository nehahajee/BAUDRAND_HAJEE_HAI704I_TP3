package fr.umontpellier.hai704i.hotel.domaine;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Offre(String id,
                    BigDecimal price,
                    int nbPersons,
                    String city,
                    int nbBeds,
                    boolean breakfast,
                    LocalDate arrivalDate,
                    LocalDate departureDate,
                    boolean available,
                    String pictureFile) {

}
