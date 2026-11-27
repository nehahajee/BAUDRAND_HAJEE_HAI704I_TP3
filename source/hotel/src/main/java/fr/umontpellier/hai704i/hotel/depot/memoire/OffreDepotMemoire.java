package fr.umontpellier.hai704i.hotel.depot.memoire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;

@Profile("memoire")
@Repository
public class OffreDepotMemoire implements OffreDepot {

    private final Map<String, Offre> offres = new ConcurrentHashMap<>();

    @Override
    public List<Offre> toutes() {
        return new ArrayList<>(offres.values());
    }

    @Override
    public Optional<Offre> parId(String id) {
        return Optional.ofNullable(offres.get(id));
    }

    @Override
    public Offre enregistrer(Offre offre) {
        offres.put(offre.id(), offre);
        return offre;
    }

    @Override
    public long compter() {
        return offres.size();
    }
}
