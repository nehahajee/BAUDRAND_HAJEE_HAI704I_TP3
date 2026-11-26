package fr.umontpellier.hai704i.hotel.api;

import fr.umontpellier.hai704i.hotel.api.vue.DemandeModification;
import fr.umontpellier.hai704i.hotel.api.vue.DemandeReservation;
import fr.umontpellier.hai704i.hotel.api.vue.ReservationVue;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
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

    private static String etag(Reservation reservation) {return "\"" + reservation.version() + "\"";}


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
                    .eTag(etag(reservation.reservation()))
                    .header("Idempotent-Replay", "true")
                    .body(vue);
        }

        return ResponseEntity.created(emplacement).eTag(etag(reservation.reservation())).body(vue);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationVue> consulter(@PathVariable String id, @RequestAttribute("agence") String agence){
        Reservation r = serviceReservation.lireReservation(id, agence);
        return ResponseEntity.ok().eTag(etag(r)).body(ReservationVue.depuis(r, base()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationVue> modifier(
            @PathVariable String id,
            @Valid @RequestBody DemandeModification demande,
            @RequestHeader(name = "If-Match", required = false) String ifMatch,
            @RequestAttribute("agence") String agence){

        Reservation r = serviceReservation.modifier(id, demande.firstName(),  demande.lastName(), demande.nbPersons(), ifMatch, agence);
        return ResponseEntity.ok().eTag(etag(r)).body(ReservationVue.depuis(r, base()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationVue> supprimer(@PathVariable String id, @RequestAttribute("agence") String agence){
        serviceReservation.annuler(id, agence);
        return ResponseEntity.noContent().build();
    }

}
