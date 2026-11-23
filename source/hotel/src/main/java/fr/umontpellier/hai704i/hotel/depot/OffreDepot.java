package fr.umontpellier.hai704i.hotel.depot;

import java.util.List;
import java.util.Optional;

import fr.umontpellier.hai704i.hotel.domaine.Offre;


public interface OffreDepot {

    List<Offre> toutes();

    Optional<Offre> parId(String id);

    Offre enregistrer(Offre offre);

    long compter();
}
