package fr.umontpellier.hai704i.hotel.api;

import java.net.URI;
import java.util.Objects;

import fr.umontpellier.hai704i.hotel.service.*;
import org.springframework.http.*;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


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

    @ExceptionHandler(PhotoIllisible.class)
    ProblemDetail photoIllisible(PhotoIllisible e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        pd.setType(URI.create("urn:hotel:photo-illisible"));
        pd.setTitle("La photo est illisible");
        return pd;
    }

    @ExceptionHandler(NonAuthentifie.class)
    public ResponseEntity<ProblemDetail> nonAuthentifie(NonAuthentifie e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        pd.setType(URI.create("urn:hotel:non-authentifie"));
        pd.setTitle("Authentification requise");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"hotel\"")
                .body(pd);
    }

    @ExceptionHandler(DemandeIncorrecte.class)
    public ProblemDetail DemandeIncorrecte(DemandeIncorrecte e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        pd.setType(URI.create("urn:hotel:photo-illisible"));
        pd.setTitle("La photo est illisible");
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

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object corps,
            HttpHeaders entetes, HttpStatusCode statut, WebRequest requete) {

        ProblemDetail pd;
        if(corps instanceof ProblemDetail) pd = (ProblemDetail) corps;
        else if (e instanceof ErrorResponse) pd = ((ErrorResponse) e).getBody();
        else{
            pd = ProblemDetail.forStatusAndDetail(statut, e.getMessage());
            pd.setType(URI.create("urn:hotel:erreur-serveur"));
            pd.setTitle("Erreur serveur");}

        if (Objects.equals(pd.getType(), URI.create("about:blank"))
                || Objects.equals(pd.getType(), null)) {
            switch (pd.getStatus()) {
                case 400:
                    pd.setType(URI.create("urn:hotel:requete-illisible"));
                    pd.setTitle("La requête est invalide");
                    break;
                case 404:
                        pd.setType(URI.create("urn:hotel:introuvable"));
                        pd.setTitle("La ressource est inexistante");
                        break;
                    case 405:
                        pd.setType(URI.create("urn:hotel:methode-non-permise"));
                        pd.setTitle("La méthode utilisée ici est non permise");
                        break;
                    case 406:
                        pd.setType(URI.create("urn:hotel:representation-non-supporte"));
                        pd.setTitle("La représentation demandée n'est pas supportée");
                        break;
                    case 415:
                        pd.setType(URI.create("urn:hotel:type-non-supporte"));
                        pd.setTitle("Le type de contenu envoyé par le client n'est pas supporté");
                        break;
                default:
                    pd.setType(URI.create("urn:hotel:erreur-serveur"));
                    pd.setTitle("Le serveur a rencontré une erreur");
                    break;
                }

            return super.handleExceptionInternal(e, pd, entetes, statut, requete);
        }

        return super.handleExceptionInternal(e, pd, entetes, statut, requete);
    }
}
