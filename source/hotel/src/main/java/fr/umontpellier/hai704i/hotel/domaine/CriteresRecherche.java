package fr.umontpellier.hai704i.hotel.domaine;

import java.time.LocalDate;

public record CriteresRecherche(String city,
                                LocalDate arrivalDate,
                                LocalDate departureDate,
                                Integer nbPersons) {

    public boolean correspond(Offre offre) {
        return (city == null || offre.city().equalsIgnoreCase(city))
                && (arrivalDate == null || offre.arrivalDate().equals(arrivalDate))
                && (departureDate == null || offre.departureDate().equals(departureDate))
                && (nbPersons == null || offre.nbPersons() >= nbPersons);
    }
}
