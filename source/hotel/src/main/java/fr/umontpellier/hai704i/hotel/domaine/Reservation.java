package fr.umontpellier.hai704i.hotel.domaine;

import java.math.BigDecimal;

public record Reservation(String id,
                          String offerId,
                          BigDecimal price,
                          int nbPersons,
                          String firstName,
                          String lastName,
                          State state,
                          String agency,
                          String idempotenceKey) {
}
