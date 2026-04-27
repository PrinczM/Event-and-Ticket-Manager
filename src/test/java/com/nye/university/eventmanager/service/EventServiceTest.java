package com.nye.university.eventmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nye.university.eventmanager.dto.event.EventRequestDto;
import com.nye.university.eventmanager.dto.event.EventResponseDto;
import com.nye.university.eventmanager.entity.Event;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.repository.EventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Az EventService egységtesztjei. */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventService eventService;

  private final LocalDateTime testDateTime = LocalDateTime.of(2026, 6, 15, 18, 0);

  private Event buildEvent() {
    return Event.builder()
        .id(1L)
        .name("Teszt Esemény")
        .location("Budapest")
        .startDateTime(testDateTime)
        .capacity(100)
        .build();
  }

  private EventRequestDto buildRequest() {
    return EventRequestDto.builder()
        .name("Teszt Esemény")
        .location("Budapest")
        .startDateTime(testDateTime)
        .capacity(100)
        .build();
  }

  @Test
  void createEventWhenRequestIsValidThenReturnSavedEvent() {
    // given
    EventRequestDto request = buildRequest();
    Event savedEvent = buildEvent();
    when(eventRepository.save(any())).thenReturn(savedEvent);

    // when
    EventResponseDto result = eventService.createEvent(request);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Teszt Esemény");
    assertThat(result.getLocation()).isEqualTo("Budapest");
    assertThat(result.getCapacity()).isEqualTo(100);
    verify(eventRepository).save(any());
  }

  @Test
  void getAllEventsWhenEmptyThenReturnEmptyList() {
    // given
    when(eventRepository.findAll()).thenReturn(List.of());

    // when
    List<EventResponseDto> result = eventService.getAllEvents();

    // then
    assertThat(result).isEmpty();
    verify(eventRepository).findAll();
  }

  @Test
  void getAllEventsWhenNotEmptyThenReturnAllEvents() {
    // given
    when(eventRepository.findAll()).thenReturn(List.of(buildEvent()));

    // when
    List<EventResponseDto> result = eventService.getAllEvents();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Teszt Esemény");
    verify(eventRepository).findAll();
  }

  @Test
  void getEventByIdWhenEventExistsThenReturnEvent() {
    // given
    when(eventRepository.findById(1L)).thenReturn(Optional.of(buildEvent()));

    // when
    EventResponseDto result = eventService.getEventById(1L);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Teszt Esemény");
    verify(eventRepository).findById(1L);
  }

  @Test
  void getEventByIdWhenEventNotFoundThenThrowException() {
    // given
    when(eventRepository.findById(99L)).thenReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> eventService.getEventById(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(eventRepository).findById(99L);
  }

  @Test
  void updateEventWhenEventExistsThenReturnUpdatedEvent() {
    // given
    Event existing = buildEvent();
    Event updated = Event.builder()
        .id(1L)
        .name("Módosított Esemény")
        .location("Debrecen")
        .startDateTime(testDateTime)
        .capacity(200)
        .build();
    EventRequestDto request = EventRequestDto.builder()
        .name("Módosított Esemény")
        .location("Debrecen")
        .startDateTime(testDateTime)
        .capacity(200)
        .build();

    when(eventRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(eventRepository.save(any())).thenReturn(updated);

    // when
    EventResponseDto result = eventService.updateEvent(1L, request);

    // then
    assertThat(result.getName()).isEqualTo("Módosított Esemény");
    assertThat(result.getCapacity()).isEqualTo(200);
    verify(eventRepository).findById(1L);
    verify(eventRepository).save(any());
  }

  @Test
  void updateEventWhenEventNotFoundThenThrowException() {
    // given
    when(eventRepository.findById(99L)).thenReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> eventService.updateEvent(99L, buildRequest()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(eventRepository).findById(99L);
  }

  @Test
  void deleteEventWhenEventExistsThenDeleteSuccessfully() {
    // given
    when(eventRepository.existsById(1L)).thenReturn(true);

    // when
    eventService.deleteEvent(1L);

    // then
    verify(eventRepository).existsById(1L);
    verify(eventRepository).deleteById(1L);
  }

  @Test
  void deleteEventWhenEventNotFoundThenThrowException() {
    // given
    when(eventRepository.existsById(99L)).thenReturn(false);

    // when + then
    assertThatThrownBy(() -> eventService.deleteEvent(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(eventRepository).existsById(99L);
  }

}
