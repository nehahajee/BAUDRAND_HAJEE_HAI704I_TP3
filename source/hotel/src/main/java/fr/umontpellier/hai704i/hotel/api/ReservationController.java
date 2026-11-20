package fr.umontpellier.hai704i.hotel.api;

import fr.umontpellier.hai704i.hotel.api.vue.DemandeReservation;
import fr.umontpellier.hai704i.hotel.api.vue.ReservationVue;
import fr.umontpellier.hai704i.hotel.service.ServiceReservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bookings")
public class ReservationController {
    private final ServiceReservation serviceReservation;

    public ReservationController(ServiceReservation serviceReservation) {
        this.serviceReservation = serviceReservation;
    }

    private static String base() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }


    @PostMapping
    public ResponseEntity<ReservationVue> reserver(
            @Valid @RequestBody DemandeReservation demande){

        var reservation = serviceReservation.creerReservation(demande.offerId(), demande.nbPersons(), demande.firstName(), demande.lastName());
        ReservationVue vue = ReservationVue.depuis(reservation, base());
        URI emplacement = URI.create(base() + "/api/v1/bookings/" + reservation.id());

        return ResponseEntity.created(emplacement).body(vue);

    }
}
