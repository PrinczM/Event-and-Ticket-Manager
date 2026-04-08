package com.nye.university.eventmanager.service;

import com.nye.university.eventmanager.dto.ticket.TicketRequestDto;
import com.nye.university.eventmanager.dto.ticket.TicketResponseDto;
import com.nye.university.eventmanager.entity.Event;
import com.nye.university.eventmanager.entity.Ticket;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.repository.EventRepository;
import com.nye.university.eventmanager.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** A jegykezelés üzleti logikáját megvalósító service osztály. */
@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final EventRepository eventRepository;

  /**
   * Létrehoz egy új jegyet.
   *
   * @param requestDto az új jegy adatai
   * @return a mentett jegy adatai
   */
  public TicketResponseDto createTicket(TicketRequestDto requestDto) {
    Event event = findEventById(requestDto.getEventId());

    Ticket ticket = Ticket.builder()
        .ownerName(requestDto.getOwnerName())
        .ticketType(requestDto.getTicketType())
        .price(requestDto.getPrice())
        .event(event)
        .build();

    Ticket savedTicket = ticketRepository.save(ticket);
    return mapToDto(savedTicket);
  }

  /**
   * Visszaadja az összes jegyet.
   *
   * @return a jegyek listája
   */
  public List<TicketResponseDto> getAllTickets() {
    return ticketRepository.findAll()
        .stream()
        .map(this::mapToDto)
        .toList();
  }

  /**
   * Visszaad egy jegyet azonosító alapján.
   *
   * @param id a jegy azonosítója
   * @return a jegy adatai
   */
  public TicketResponseDto getTicketById(Long id) {
    Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ticket not found by id: " + id));
    return mapToDto(ticket);
  }

  /**
   * Módosít egy meglévő jegyet.
   *
   * @param id a módosítandó jegy azonosítója
   * @param requestDto a frissített adatok
   * @return a frissített jegy adatai
   */
  public TicketResponseDto updateTicket(Long id, TicketRequestDto requestDto) {
    Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ticket not found by id: " + id));

    Event event = findEventById(requestDto.getEventId());

    ticket.setOwnerName(requestDto.getOwnerName());
    ticket.setTicketType(requestDto.getTicketType());
    ticket.setPrice(requestDto.getPrice());
    ticket.setEvent(event);

    Ticket updatedTicket = ticketRepository.save(ticket);
    return mapToDto(updatedTicket);
  }

  /**
   * Töröl egy jegyet azonosító alapján.
   *
   * @param id a törlendő jegy azonosítója
   */
  public void deleteTicket(Long id) {
    if (!ticketRepository.existsById(id)) {
      throw new ResourceNotFoundException("Ticket not found by id: " + id);
    }
    ticketRepository.deleteById(id);
  }

  private Event findEventById(Long id) {
    return eventRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found by id: " + id));
  }

  private TicketResponseDto mapToDto(Ticket ticket) {
    Event event = ticket.getEvent();
    return TicketResponseDto.builder()
        .id(ticket.getId())
        .ownerName(ticket.getOwnerName())
        .ticketType(ticket.getTicketType())
        .price(ticket.getPrice())
        .eventId(event != null ? event.getId() : null)
        .eventName(event != null ? event.getName() : null)
        .build();
  }

}
