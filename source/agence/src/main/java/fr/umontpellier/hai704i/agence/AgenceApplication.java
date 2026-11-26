package fr.umontpellier.hai704i.agence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AgenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgenceApplication.class, args);
    }

}
