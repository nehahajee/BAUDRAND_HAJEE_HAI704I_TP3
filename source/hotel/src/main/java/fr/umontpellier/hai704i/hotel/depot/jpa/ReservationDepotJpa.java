package fr.umontpellier.hai704i.hotel.depot.jpa;


import fr.umontpellier.hai704i.hotel.depot.ReservationDepot;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Profile("!memoire")
public class ReservationDepotJpa implements ReservationDepot{
    private final ReservationJpa jpa;

    ReservationDepotJpa(ReservationJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> toutes() {
        List<ReservationEntite> entites = jpa.findAll();
        return entites.stream().map(ReservationEntite::versDomaine).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> parId(String id) {
        return jpa.findById(id).map(ReservationEntite::versDomaine);
    }

    @Override
    public Reservation enregistrer(Reservation reservation) {
        jpa.save(ReservationEntite.depuis(reservation));
        return reservation;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> parCle(String agence, String cle) {
        return jpa.findByAgencyAndIdempotenceKey(agence, cle).map(ReservationEntite::versDomaine);
    }

    @Override
    @Transactional(readOnly = true)
    public String prochainId() {
        List<Reservation> reservations = toutes();
        OptionalInt maxId =  reservations.stream().map(Reservation::id).mapToInt(id -> Integer.parseInt(id.substring(2))).max();
        return "B-"+(maxId.orElse(1000)+1);
    }
}
