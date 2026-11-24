package fr.umontpellier.hai704i.agence.api;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.umontpellier.hai704i.agence.service.HotelIndisponible;
import fr.umontpellier.hai704i.agence.service.Panne;
import fr.umontpellier.hai704i.agence.service.RefusHotel;

/**
 * Traduit les fautes de l'agence en application/problem+json.
 * Un refus de l'hôtel n'est pas une panne : il n'a pas le même code.
 */
@RestControllerAdvice
public class GestionErreurs extends ResponseEntityExceptionHandler {

    /** En lecture, le seul refus possible de l'hôtel est l'offre inconnue (à affiner en A5 : 409). */
    @ExceptionHandler(RefusHotel.class)
    ProblemDetail refus(RefusHotel e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        pd.setType(URI.create("urn:agence:refus-hotel"));
        pd.setTitle("Refus de l'hôtel");
        return pd;
    }

    /** Le problème est en amont : 504 si l'hôtel est trop lent, 502 sinon (A6.5). */
    @ExceptionHandler(HotelIndisponible.class)
    ProblemDetail indisponible(HotelIndisponible e) {
        HttpStatus statut = e.getNature() == Panne.TROP_LENT
                ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.BAD_GATEWAY;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(statut, e.getMessage());
        pd.setType(URI.create("urn:agence:hotel-indisponible"));
        pd.setTitle("Hôtel indisponible");
        pd.setProperty("nature", e.getNature());
        return pd;
    }
}
