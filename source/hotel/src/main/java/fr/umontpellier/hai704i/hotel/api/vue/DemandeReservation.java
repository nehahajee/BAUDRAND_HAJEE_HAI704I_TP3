package fr.umontpellier.hai704i.hotel.api.vue;

import fr.umontpellier.hai704i.hotel.domaine.State;
import jakarta.validation.constraints.*;

public record DemandeReservation(
        @NotBlank(message = "l'offre est obligatoire")
        @Pattern(regexp = "O-\\d{4}", message = "id attendu sous la forme O-0000")
        String offerId,

        @NotNull(message = "le nombre de personnes est obligatoire")
        @Positive(message = "le nombre de personnes doit être supérieur à 0")
        Integer nbPersons,

        @NotBlank(message = "le prénom est obligatoire")
        @Size(min = 2, max = 60, message = "le prénom doit compter de 2 à 60 caractères")
        String firstName,

        @NotBlank(message = "le nom est obligatoire")
        @Size(min = 2, max = 60, message = "le nom doit compter de 2 à 60 caractères")
        String lastName
) {

}
