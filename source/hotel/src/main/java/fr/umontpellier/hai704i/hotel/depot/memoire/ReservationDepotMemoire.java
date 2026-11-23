package fr.umontpellier.hai704i.hotel.depot.memoire;

import fr.umontpellier.hai704i.hotel.depot.ReservationDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;
import fr.umontpellier.hai704i.hotel.domaine.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReservationDepotMemoire implements ReservationDepot {
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public List<Reservation> toutes() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Optional<Reservation> parId(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public Reservation enregistrer(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
        return reservation;
    }
}
