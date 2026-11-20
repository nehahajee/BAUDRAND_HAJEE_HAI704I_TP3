package fr.umontpellier.hai704i.hotel.depot;


import fr.umontpellier.hai704i.hotel.domaine.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDepot {

    List<Reservation> toutes();

    Optional<Reservation> parId(String id);

    Reservation enregistrer(Reservation reservation);
}
