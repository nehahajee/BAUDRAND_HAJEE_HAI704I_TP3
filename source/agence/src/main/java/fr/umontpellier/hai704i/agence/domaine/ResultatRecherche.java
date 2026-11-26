package fr.umontpellier.hai704i.agence.domaine;

import java.util.List;

public record ResultatRecherche(List<Offre> offres, List<Incident> incidents) {
}
