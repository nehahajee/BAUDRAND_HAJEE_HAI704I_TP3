package fr.umontpellier.hai704i.hotel.api.vue;

import fr.umontpellier.hai704i.hotel.domaine.Reservation;

import java.math.BigDecimal;

public record ReservationVue(
        String id,
        String offerLink,
        BigDecimal price,
        int nbPersons,
        String firstName,
        String lastName,
        String state) {

    public static ReservationVue depuis(Reservation reservation, String base) {
        return new ReservationVue(
                reservation.id(),
                base + "/api/v1/offers/" + reservation.offerId(),
                reservation.price(),
                reservation.nbPersons(),
                reservation.firstName(),
                reservation.lastName(),
                reservation.state().toString()
        );
    }
}
