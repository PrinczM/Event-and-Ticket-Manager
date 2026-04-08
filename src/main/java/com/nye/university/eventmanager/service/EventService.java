package com.nye.university.eventmanager.service;

import com.nye.university.eventmanager.dto.event.EventRequestDto;
import com.nye.university.eventmanager.dto.event.EventResponseDto;
import com.nye.university.eventmanager.entity.Event;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Az eseménykezelés üzleti logikáját megvalósító service osztály. */
@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  /**
   * Létrehoz egy új eseményt.
   *
   * @param requestDto az új esemény adatai
   * @return a mentett esemény adatai
   */
  public EventResponseDto createEvent(EventRequestDto requestDto) {
    Event event = Event.builder()
        .name(requestDto.getName())
        .location(requestDto.getLocation())
        .startDateTime(requestDto.getStartDateTime())
        .capacity(requestDto.getCapacity())
        .build();
    Event savedEvent = eventRepository.save(event);
    return mapToDto(savedEvent);
  }

  /**
   * Visszaadja az összes eseményt.
   *
   * @return az események listája
   */
  public List<EventResponseDto> getAllEvents() {
    return eventRepository.findAll()
        .stream()
        .map(this::mapToDto)
        .toList();
  }

  /**
   * Visszaad egy eseményt azonosító alapján.
   *
   * @param id az esemény azonosítója
   * @return az esemény adatai
   */
  public EventResponseDto getEventById(Long id) {
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found by id: " + id));
    return mapToDto(event);
  }

  /**
   * Módosít egy meglévő eseményt.
   *
   * @param id a módosítandó esemény azonosítója
   * @param requestDto a frissített adatok
   * @return a frissített esemény adatai
   */
  public EventResponseDto updateEvent(Long id, EventRequestDto requestDto) {
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found by id: " + id));

    event.setName(requestDto.getName());
    event.setLocation(requestDto.getLocation());
    event.setStartDateTime(requestDto.getStartDateTime());
    event.setCapacity(requestDto.getCapacity());

    Event updatedEvent = eventRepository.save(event);
    return mapToDto(updatedEvent);
  }

  /**
   * Töröl egy eseményt azonosító alapján.
   *
   * @param id a törlendő esemény azonosítója
   */
  public void deleteEvent(Long id) {
    if (!eventRepository.existsById(id)) {
      throw new ResourceNotFoundException("Event not found by id: " + id);
    }
    eventRepository.deleteById(id);
  }

  private EventResponseDto mapToDto(Event event) {
    return EventResponseDto.builder()
        .id(event.getId())
        .name(event.getName())
        .location(event.getLocation())
        .startDateTime(event.getStartDateTime())
        .capacity(event.getCapacity())
        .build();
  }

}
