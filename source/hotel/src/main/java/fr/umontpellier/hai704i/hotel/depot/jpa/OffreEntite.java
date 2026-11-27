package fr.umontpellier.hai704i.hotel.depot.jpa;

import fr.umontpellier.hai704i.hotel.domaine.Offre;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offre")
public class OffreEntite {
    @Id
    private String id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private int nbPersons;

    @Column(nullable = false)
    private String city;

    private int nbBeds;

    private boolean breakfast;

    @Column(nullable = false)
    private LocalDate arrivalDate;

    @Column(nullable = false)
    private LocalDate departureDate;

    private boolean available;

    private String pictureFile;

    protected OffreEntite() { }

    static OffreEntite depuis(Offre o) {
        OffreEntite entite = new OffreEntite();
        entite.id = o.id();
        entite.price = o.price();
        entite.nbPersons = o.nbPersons();
        entite.city = o.city();
        entite.nbBeds = o.nbBeds();
        entite.breakfast = o.breakfast();
        entite.arrivalDate = o.arrivalDate();
        entite.departureDate = o.departureDate();
        entite.available = o.available();
        entite.pictureFile = o.pictureFile();
        return entite;
    }

    Offre versDomaine() {
        return new Offre(id, price, nbPersons, city, nbBeds, breakfast, arrivalDate, departureDate, available, pictureFile);
    }
}
