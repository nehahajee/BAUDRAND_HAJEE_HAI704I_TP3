package fr.umontpellier.hai704i.agence.client;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import fr.umontpellier.hai704i.agence.domaine.Offre;
import fr.umontpellier.hai704i.agence.service.Hotel;
import fr.umontpellier.hai704i.agence.service.HotelIndisponible;
import fr.umontpellier.hai704i.agence.service.Panne;
import fr.umontpellier.hai704i.agence.service.RefusHotel;

import java.net.SocketTimeoutException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


public class ClientHotel implements Hotel {
    private final RestClient client;
    private final String nom;
    private final String base;

    public ClientHotel(RestClient.Builder builder,
                String nom, String base, Duration delai) {
        this.nom = nom;
        this.base = base;
        this.client = builder.clone()
                .baseUrl(base)
                .requestFactory(ClientHttpRequestFactoryBuilder.detect()
                        .build(HttpClientSettings.defaults().withTimeouts(delai, delai)))
                .build();
    }

    @Override
    public List<Offre> chercher(String ville, LocalDate arrivee,
                                LocalDate depart, Integer personnes) {
        var reponse = appeler(() -> client.get()
                .uri(u -> u.path("/api/v1/offers")
                        .queryParamIfPresent("city", Optional.ofNullable(ville))
                        .queryParamIfPresent("arrivalDate", Optional.ofNullable(arrivee))
                        .queryParamIfPresent("departureDate", Optional.ofNullable(depart))
                        .queryParamIfPresent("nbPersons", Optional.ofNullable(personnes))
                        .build())
                .retrieve()
                .toEntity(PageOffresHotel.class));
        PageOffresHotel page = reponse.getBody();
        if (page == null || page.elements() == null) {
            throw new HotelIndisponible(Panne.ERREUR_SERVEUR, "nom: " + nom +
                    "(" + base + "): " + " a répondu " + reponse.getStatusCode().value() + " sans liste d'offres");
        }
        return page.elements().stream().map(ClientHotel::versDomaine).toList();
    }

    @Override
    public Offre offre(String offerId) {
        var reponse = appeler(() -> client.get()
                .uri("/api/v1/offers/{id}", offerId)
                .retrieve()
                .toEntity(OffreHotelVue.class));
        OffreHotelVue offre = reponse.getBody();
        if (offre == null) {
            throw new HotelIndisponible(Panne.ERREUR_SERVEUR,
                    "nom: " + nom + "(" + base + "): " + " a répondu " + reponse.getStatusCode().value() + " sans offre");
        }
        return versDomaine(offre);
    }

    /** Le JSON de l'hôtel devient une offre du domaine : seul le client connaît les deux. */
    private static Offre versDomaine(OffreHotelVue v) {
        return new Offre(v.id(), v.hotelName(), v.city(), v.price(), v.nbPersons(),
                v.nbBeds(), v.breakfast(), v.arrivalDate(), v.departureDate());
    }

    private <T> T appeler(Supplier<T> appel) {
        try {
            return appel.get();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new RefusHotel(detail(e));
            }
            throw new HotelIndisponible(Panne.ERREUR_SERVEUR,
                    "nom: " + nom + "(" + base + "): " + " a répondu " + e.getStatusCode().value() + " : " + detail(e));
        } catch (ResourceAccessException e) {
            Throwable cause = e.getMostSpecificCause();
            boolean delai = !(cause instanceof HttpConnectTimeoutException)
                    && (cause instanceof SocketTimeoutException
                        || cause instanceof HttpTimeoutException);
            Panne nature = delai ? Panne.TROP_LENT : Panne.INJOIGNABLE;
            throw new HotelIndisponible(nature, "nom: " + nom +  "(" + base + "):" + message(cause) );
        }
    }

    private static String detail(RestClientResponseException e) {
        ProblemDetail p = e.getResponseBodyAs(ProblemDetail.class);
        return (p != null && p.getDetail() != null) ? p.getDetail() : e.getStatusText();
    }

    private static String message(Throwable cause) {
        String m = cause.getMessage();
        return (m == null || m.isBlank()) ? cause.getClass().getSimpleName() : m;
    }
}
