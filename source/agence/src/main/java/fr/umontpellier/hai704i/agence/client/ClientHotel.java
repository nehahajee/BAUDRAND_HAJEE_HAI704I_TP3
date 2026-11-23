package fr.umontpellier.hai704i.agence.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ClientHotel {
    private final RestClient client;

    ClientHotel(RestClient.Builder builder,
                @Value("${agence.hotels[0].base}") String base,
                @Value("${agence.delai}") Duration delai) {
        this.client = builder.clone()
                .baseUrl(base)
                .requestFactory(ClientHttpRequestFactoryBuilder.detect()
                        .build(HttpClientSettings.defaults().withTimeouts(delai, delai)))
                .build();
    }

    public List<OffreHotelVue> chercher(String ville, LocalDate arrivee,
                                        LocalDate depart, Integer personnes) {
        try {
            var reponse = client.get()
                    .uri(u -> u.path("/api/v1/offers")
                            .queryParamIfPresent("city", Optional.ofNullable(ville))
                            .queryParamIfPresent("arrivalDate", Optional.ofNullable(arrivee))
                            .queryParamIfPresent("departureDate", Optional.ofNullable(depart))
                            .queryParamIfPresent("nbPersons", Optional.ofNullable(personnes))
                            .build())
                    .retrieve()
                    .toEntity(PageOffresHotel.class);
            return reponse.getBody().elements();

        } catch (RestClientResponseException e) {
            // l'hôtel a répondu une erreur (4xx refus / 5xx panne) : à traduire en A4.4
            throw e;
        } catch (ResourceAccessException e) {
            // l'hôtel n'a pas répondu (lent / injoignable) : à traduire en A4.4
            throw e;
        }
    }

    public Boolean offre(id){

    }
}