package fr.umontpellier.hai704i.hotel.depot.jpa;

import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import fr.umontpellier.hai704i.hotel.domaine.State;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "reservation", uniqueConstraints = @UniqueConstraint(columnNames = {"agency", "idempotenceKey"}))
public class ReservationEntite {
    @Id
    private String id;

    @Column(nullable = false)
    private String offerId;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int nbPersons;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private String agency;

    @Column(nullable = false)
    private String idempotenceKey;

    private int version;

    protected ReservationEntite() { }

    static ReservationEntite depuis(Reservation r) {
        ReservationEntite entite = new ReservationEntite();
        entite.id = r.id();
        entite.offerId = r.offerId();
        entite.price = r.price();
        entite.nbPersons = r.nbPersons();
        entite.firstName = r.firstName();
        entite.lastName = r.lastName();
        entite.state = r.state();
        entite.agency = r.agency();
        entite.idempotenceKey = r.idempotenceKey();
        entite.version = r.version();
        return entite;
    }


    Reservation versDomaine() {
        return new Reservation(id, offerId, price, nbPersons, firstName, lastName, state, agency, idempotenceKey, version);
    }
}
