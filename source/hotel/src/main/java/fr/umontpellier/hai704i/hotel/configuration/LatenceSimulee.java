package fr.umontpellier.hai704i.hotel.configuration;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Simule un hôtel lent, pour l'étape A6 : chaque requête attend {@code hotel.latence}
 * avant d'être traitée. Vaut 0 par défaut ; se règle au lancement :
 * {@code --hotel.latence=5s}.
 */
@Component
public class LatenceSimulee extends OncePerRequestFilter {

    private final Duration latence;

    public LatenceSimulee(@Value("${hotel.latence:0s}") Duration latence) {
        this.latence = latence;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requete, HttpServletResponse reponse,
                                    FilterChain suite) throws ServletException, IOException {
        if (!latence.isZero()) {
            try {
                Thread.sleep(latence.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        suite.doFilter(requete, reponse);
    }
}
