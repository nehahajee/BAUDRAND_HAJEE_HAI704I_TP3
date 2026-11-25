package fr.umontpellier.hai704i.agence.api.vue;

import fr.umontpellier.hai704i.agence.domaine.Incident;

/** La représentation d'un hôtel qui n'a pas pu répondre pendant une recherche. */
public record IncidentVue(String hotel, String nature, String detail) {

    public static IncidentVue depuis(Incident i) {
        return new IncidentVue(i.hotel(), i.nature().name(), i.detail());
    }
}
