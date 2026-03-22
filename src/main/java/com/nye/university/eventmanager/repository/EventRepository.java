package com.nye.university.eventmanager.repository;

import com.nye.university.eventmanager.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Az Event entitás adatbázis-műveleteihez használt repository. */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
