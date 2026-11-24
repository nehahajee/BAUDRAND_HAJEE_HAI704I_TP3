package fr.umontpellier.hai704i.agence.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.umontpellier.hai704i.agence.api.vue.OffreVue;
import fr.umontpellier.hai704i.agence.service.ServiceAgence;

/**
 * Le point d'accès HTTP de l'agence : il traduit une requête en appel de service, sans règle métier.
 * Les échecs de l'hôtel sont traduits en réponses HTTP par {@link GestionErreurs}.
 */
@RestController
@RequestMapping("/api/v1/offers")
public class OffresController {

    private final ServiceAgence service;

    public OffresController(ServiceAgence service) {
        this.service = service;
    }

    @GetMapping
    public List<OffreVue> rechercher(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate arrivalDate,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false) Integer nbPersons) {

        return service.rechercher(city, arrivalDate, departureDate, nbPersons).stream()
                .map(OffreVue::depuis)
                .toList();
    }

    @GetMapping("/{id}")
    public OffreVue consulter(@PathVariable String id) {
        return OffreVue.depuis(service.offre(id));
    }
}
