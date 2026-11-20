package fr.umontpellier.hai704i.hotel.api;

import java.net.URI;

import fr.umontpellier.hai704i.hotel.service.Conflit;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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

    @ExceptionHandler(Conflit.class)
    ProblemDetail conflit(Conflit e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        pd.setType(URI.create("urn:hotel:conflit"));
        pd.setTitle("Conflit avec l'état courant");
        return pd;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders entetes,
            HttpStatusCode statut, WebRequest requete) {

        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .reduce((a, b) -> a + " ; " + b)
                .orElse("demande invalide");

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, detail);
        pd.setType(URI.create("urn:hotel:demande-invalide"));
        pd.setTitle("Demande invalide");
        return ResponseEntity.unprocessableEntity().body(pd);
    }
}
