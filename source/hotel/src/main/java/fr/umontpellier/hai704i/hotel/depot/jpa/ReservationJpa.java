package fr.umontpellier.hai704i.hotel.depot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ReservationJpa extends JpaRepository<ReservationEntite, String> {

    Optional<ReservationEntite> findByAgencyAndIdempotenceKey(String agency, String idempotenceKey);
}
