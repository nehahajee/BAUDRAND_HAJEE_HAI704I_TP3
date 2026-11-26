package fr.umontpellier.hai704i.hotel.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

@ConfigurationProperties(prefix = "hotel")
public record AgencesConnues(Map<String, String> agences) {}

