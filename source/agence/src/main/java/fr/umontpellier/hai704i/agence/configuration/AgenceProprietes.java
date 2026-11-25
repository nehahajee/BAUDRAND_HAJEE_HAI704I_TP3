package fr.umontpellier.hai704i.agence.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "agence")
public record AgenceProprietes(Duration delai, List<HotelConfig> hotels) {
    /**Un hotel interrogé par l'agence */
    public record HotelConfig(String nom, String base){}
}