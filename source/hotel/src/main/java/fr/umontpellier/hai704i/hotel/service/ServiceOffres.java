package fr.umontpellier.hai704i.hotel.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

import fr.umontpellier.hai704i.hotel.domaine.Photo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import fr.umontpellier.hai704i.hotel.depot.OffreDepot;
import fr.umontpellier.hai704i.hotel.domaine.CriteresRecherche;
import fr.umontpellier.hai704i.hotel.domaine.Offre;

import java.security.MessageDigest;

@Service
public class ServiceOffres {

    private final OffreDepot depot;

    public ServiceOffres(OffreDepot depot) {
        this.depot = depot;
    }


    public List<Offre> rechercher(CriteresRecherche criteres) {
        return depot.toutes().stream()
                .filter(Offre::available)
                .filter(criteres::correspond)
                .sorted(Comparator.comparing(Offre::id))
                .toList();
    }


    public Offre parId(String id) {
        return depot.parId(id)
                .orElseThrow(() -> new Introuvable("Aucune offre d'identifiant " + id + "."));
    }

    public Photo lirePhoto(String id) {
         String nomPhoto = parId(id).pictureFile();
         if (nomPhoto == null) {
             throw new Introuvable(" Cette offre ne contient pas de photo " + id + ".");
         }

        ClassPathResource ressource = new ClassPathResource("photos/" + nomPhoto);
        if (!ressource.exists()) {
            throw new Introuvable(" Cette offre ne contient pas de photo " + id + ".");
        }

        byte[] contenu;
        try (InputStream in = ressource.getInputStream()) {
            contenu = in.readAllBytes();
        } catch (IOException e) {
            throw new PhotoIllisible("Impossible de lire l'image de l'offre d'identifiant " + id + ".");
        }

        String empreinte;
        try {
            byte[] hachage = MessageDigest.getInstance("SHA-256").digest(contenu);
            empreinte = HexFormat.of().formatHex(hachage);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
        return new Photo(contenu, empreinte);
    }
}
