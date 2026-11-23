package fr.umontpellier.hai704i.hotel.configuration;

import fr.umontpellier.hai704i.hotel.service.NonAuthentifie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@Component
public class AuthentificationAgence implements HandlerInterceptor {
    private final AgencesConnues agencesConnues;

    public AuthentificationAgence(AgencesConnues agencesConnues) {
        this.agencesConnues = agencesConnues;
    }

    @Override
    public boolean preHandle(HttpServletRequest requete, HttpServletResponse reponse, Object handler) {
        String entete = requete.getHeader(HttpHeaders.AUTHORIZATION);

        String prefixe = "Bearer ";
        if (entete == null || !entete.startsWith(prefixe)) {
            throw new NonAuthentifie("En-tête Authorization absent ou schéma autre que Bearer");
        }

        String secret = entete.substring(prefixe.length()).trim();

        for (Map.Entry<String, String> agence : agencesConnues.agences().entrySet()) {
            byte[] attendu = agence.getValue().getBytes(StandardCharsets.UTF_8);
            byte[] recu    = secret.getBytes(StandardCharsets.UTF_8);
            if (MessageDigest.isEqual(attendu, recu)) {
                requete.setAttribute("agence", agence.getKey());   // on retient qui appelle
                return true;
            }
        }
        throw new NonAuthentifie("Secret inconnu");

    }
}

