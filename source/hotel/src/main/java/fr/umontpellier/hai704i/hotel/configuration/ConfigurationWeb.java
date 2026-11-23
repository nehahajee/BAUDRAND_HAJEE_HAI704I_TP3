package fr.umontpellier.hai704i.hotel.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(AgencesConnues.class)
public class ConfigurationWeb implements WebMvcConfigurer {

    private final AuthentificationAgence authentification;

    public ConfigurationWeb(AuthentificationAgence authentification) {
        this.authentification = authentification;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registre) {
        registre.addInterceptor(authentification)
                .addPathPatterns("/api/v1/bookings/**");   // /** couvre aussi /api/v1/bookings lui-même
    }
}
