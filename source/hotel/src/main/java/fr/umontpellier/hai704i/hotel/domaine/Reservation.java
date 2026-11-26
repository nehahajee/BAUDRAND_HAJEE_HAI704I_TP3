package fr.umontpellier.hai704i.hotel.domaine;

import java.math.BigDecimal;

public record Reservation(String id,
                          String offerId,
                          BigDecimal price,
                          int nbPersons,
                          String firstName,
                          String lastName,
                          State state,
                          String agency,
                          String idempotenceKey,
                          int version) {

    public Reservation avec(String firstName, String lastName, Integer nbPersons){
        return modifiee( firstName == null ? this.firstName : firstName,
                lastName == null ? this.lastName : lastName,
                nbPersons == null ? this.nbPersons : nbPersons );
    }

    public Reservation modifiee(String firstName, String lastName, int nbPersons){
        return new Reservation(id, offerId, price, nbPersons, firstName, lastName, state, agency, idempotenceKey, version+1);
    }

    public Reservation annulee(){
        return new Reservation(id, offerId, price, nbPersons, firstName, lastName, State.CANCELED, agency, idempotenceKey, version+1);
    }
}
