package fr.umontpellier.hai704i.agence.domaine;

/** Un hôtel qui n'a pas pu répondre pendant une recherche : lequel, et pourquoi. */
public record Incident(String hotel, Panne nature, String detail) {
}
