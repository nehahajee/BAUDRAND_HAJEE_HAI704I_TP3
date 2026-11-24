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
            @Valid @RequestBody DemandeReservation demande,
            @RequestHeader(name = "Idempotency-Key") String cle,@RequestAttribute("agence") String agence){

        var reservation = serviceReservation.creerReservation(demande.offerId(), demande.nbPersons(), demande.firstName(), demande.lastName(), cle, agence);
        ReservationVue vue = ReservationVue.depuis(reservation.reservation(), base());
        URI emplacement = URI.create(base() + "/api/v1/bookings/" + reservation.reservation().id());

        if (reservation.rejeu()) {
            return ResponseEntity.ok()
                    .location(emplacement)
                    .header("Idempotent-Replay", "true")
                    .body(vue);
        }

        return ResponseEntity.created(emplacement).body(vue);

    }

    @GetMapping("/{id}")
    public ReservationVue consulter(@PathVariable String id){return ReservationVue.depuis(serviceReservation.lireReservation(id), base());}
}
