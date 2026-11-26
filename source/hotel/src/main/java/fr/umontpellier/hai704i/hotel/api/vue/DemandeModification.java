package fr.umontpellier.hai704i.hotel.api.vue;

import jakarta.validation.constraints.*;

public record DemandeModification(
        @Positive(message = "le nombre de personnes doit être supérieur à 0")
        Integer nbPersons,

        @Size(min = 2, max = 60, message = "le prénom doit compter de 2 à 60 caractères")
        String firstName,

        @Size(min = 2, max = 60, message = "le nom doit compter de 2 à 60 caractères")
        String lastName
) {

}

