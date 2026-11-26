package fr.umontpellier.hai704i.agence.configuration;

import fr.umontpellier.hai704i.agence.client.ClientHotel;
import fr.umontpellier.hai704i.agence.service.Hotel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class Assemblage {
    @Bean
    public List<Hotel> hotels(RestClient.Builder builder, AgenceProprietes proprietes){
        return proprietes.hotels().stream()
                .map(h ->(Hotel) new ClientHotel(builder, h.nom(), h.base(), proprietes.delai()))
                .toList();
    }
}
