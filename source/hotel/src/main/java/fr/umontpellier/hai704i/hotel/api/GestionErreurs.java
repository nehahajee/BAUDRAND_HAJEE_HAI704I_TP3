package fr.umontpellier.hai704i.hotel.api;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.umontpellier.hai704i.hotel.service.Introuvable;


@RestControllerAdvice
public class GestionErreurs extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Introuvable.class)
    ProblemDetail introuvable(Introuvable e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        pd.setType(URI.create("urn:hotel:introuvable"));
        pd.setTitle("Ressource introuvable");
        return pd;
    }
}
