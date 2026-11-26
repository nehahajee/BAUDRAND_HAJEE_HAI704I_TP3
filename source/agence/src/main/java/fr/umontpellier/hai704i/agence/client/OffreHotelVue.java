package fr.umontpellier.hai704i.agence.client;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Ce que l'agence lit d'une offre de l'hôtel.
 * C'est sa propre lecture du JSON, pas une copie de la classe de l'hôtel :
 * les noms des champs suivent ceux du JSON publié par l'hôtel.
 */
public record OffreHotelVue(String id,
                            BigDecimal price,
                            int nbPersons,
                            String city,
                            int nbBeds,
                            boolean breakfast,
                            LocalDate arrivalDate,
                            LocalDate departureDate,
                            boolean available,
                            String hotelName,
                            String pictureLink) {
}
