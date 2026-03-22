package com.nye.university.eventmanager.repository;

import com.nye.university.eventmanager.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** A Ticket entitás adatbázis-műveleteihez használt repository. */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
