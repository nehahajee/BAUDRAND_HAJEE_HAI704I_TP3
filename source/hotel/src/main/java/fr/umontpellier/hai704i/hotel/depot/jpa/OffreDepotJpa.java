package fr.umontpellier.hai704i.hotel.depot.jpa;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoire")
public class OffreDepotJpa implements OffreDepot {
    private final OffreJpa jpa;

    public OffreDepotJpa(OffreJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Offre> toutes() {
        List<OffreEntite> entites = jpa.findAll();
        return entites.stream().map(OffreEntite::versDomaine).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Offre> parId(String id) {
        return jpa.findById(id).map(OffreEntite::versDomaine);
    }

    @Override
    public Offre enregistrer(Offre offre) {
        jpa.save(OffreEntite.depuis(offre));
        return offre;
    }

    @Override
    @Transactional(readOnly = true)
    public long compter() {
        return jpa.count();
    }
}
