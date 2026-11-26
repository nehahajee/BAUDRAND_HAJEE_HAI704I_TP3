package fr.umontpellier.hai704i.agence.service;

import fr.umontpellier.hai704i.agence.domaine.Incident;

import java.util.List;

public class AmontIndisponible extends RuntimeException {
    private final List<Incident> incidents;
    public AmontIndisponible(List<Incident> incidents) {
        super("aucun hotel n'a pu répondre.");
        this.incidents = incidents;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }
}
