package fr.umontpellier.hai704i.agence.service;

public class HotelIndisponible extends RuntimeException {
    private final Panne nature;

    public HotelIndisponible(Panne nature, String detailMessage) {
        super("Erreur de nature: " + nature + ", " + detailMessage);
        this.nature = nature;
    }

    public Panne getNature() {
        return nature;
    }
}
