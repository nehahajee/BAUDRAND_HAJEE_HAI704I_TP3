package fr.umontpellier.hai704i.agence.api.vue;

import java.util.List;

import fr.umontpellier.hai704i.agence.domaine.ResultatRecherche;

/**
 * L'enveloppe d'une recherche : les offres obtenues, et les hôtels qui n'ont pas répondu.
 * Une liste d'incidents vide signifie que le résultat est complet.
 */
public record RechercheVue(List<OffreVue> elements, long total, List<IncidentVue> incidents) {

    public static RechercheVue depuis(ResultatRecherche r) {
        return new RechercheVue(
                r.offres().stream().map(OffreVue::depuis).toList(),
                r.offres().size(),
                r.incidents().stream().map(IncidentVue::depuis).toList());
    }
}
