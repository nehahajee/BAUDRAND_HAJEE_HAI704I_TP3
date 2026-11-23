package fr.umontpellier.hai704i.hotel.configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.domaine.Offre;

/**
 * Jeu de données fictif, chargé au démarrage si le dépôt est vide.
 *
 * <p>Le sujet ne demande pas de créer des offres par l'API : elles existent
 * dès le lancement. O-1004 est déjà prise, pour pouvoir vérifier qu'elle
 * n'apparaît pas dans la recherche mais reste consultable par son identifiant.
 */
@Configuration
public class DonneesInitiales {

    @Bean
    ApplicationRunner amorcer(OffreDepot depot) {
        return args -> {
            if (depot.compter() > 0) {
                return;
            }
            LocalDate d12 = LocalDate.of(2026, 12, 12);
            LocalDate d14 = LocalDate.of(2026, 12, 14);
            LocalDate d19 = LocalDate.of(2026, 12, 19);
            LocalDate d21 = LocalDate.of(2026, 12, 21);

            depot.enregistrer(new Offre("O-1001", new BigDecimal("89.00"), 2, "Montpellier",
                    1, true, d12, d14, true, "chambre-1001.jpg"));
            depot.enregistrer(new Offre("O-1002", new BigDecimal("129.00"), 4, "Montpellier",
                    2, true, d12, d14, true, "chambre-1002.jpg"));
            depot.enregistrer(new Offre("O-1003", new BigDecimal("69.00"), 1, "Montpellier",
                    1, false, d19, d21, true, "chambre-1003.jpg"));
            depot.enregistrer(new Offre("O-1004", new BigDecimal("99.00"), 2, "Montpellier",
                    1, false, d19, d21, false, "chambre-1001.jpg"));
            depot.enregistrer(new Offre("O-1005", new BigDecimal("75.00"), 2, "Nîmes",
                    1, true, d12, d14, true, null));
        };
    }
}
