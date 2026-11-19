package fr.umontpellier.hai704i.hotel.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import fr.umontpellier.hai704i.hotel.domaine.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.umontpellier.hai704i.hotel.api.vue.OffreVue;
import fr.umontpellier.hai704i.hotel.api.vue.PageVue;
import fr.umontpellier.hai704i.hotel.domaine.CriteresRecherche;
import fr.umontpellier.hai704i.hotel.service.ServiceOffres;


@RestController
@RequestMapping("/api/v1/offers")
public class OffresController {

    private final ServiceOffres service;
    private final String hotelName;

    public OffresController(ServiceOffres service,
                            @Value("${hotel.nom}") String hotelName) {
        this.service = service;
        this.hotelName = hotelName;
    }


    private static String base() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }


    @GetMapping
    public PageVue<OffreVue> rechercher(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate arrivalDate,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false) Integer nbPersons) {

        CriteresRecherche criteres =
                new CriteresRecherche(city, arrivalDate, departureDate, nbPersons);
        String base = base();
        List<OffreVue> vues = service.rechercher(criteres).stream()
                .map(o -> OffreVue.depuis(o, base, hotelName))
                .toList();
        String self = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        return new PageVue<>(vues, vues.size(), Map.of("self", self));
    }


    @GetMapping("/{id}")
    public OffreVue consulter(@PathVariable String id) {
        return OffreVue.depuis(service.parId(id), base(), hotelName);
    }

    @GetMapping(value = "/{id}/picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> lireImage(@PathVariable String id) {
        Photo photo = service.lirePhoto(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .eTag(photo.empreinte())
                .body(photo.contenu());
    }
}
