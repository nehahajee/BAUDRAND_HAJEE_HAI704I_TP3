package fr.umontpellier.hai704i.agence.client;

import java.util.List;

/** L'enveloppe de la collection d'offres renvoyée par l'hôtel. */
public record PageOffresHotel(List<OffreHotelVue> elements, long total) {
}
