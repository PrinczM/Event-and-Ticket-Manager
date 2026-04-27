package com.nye.university.eventmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nye.university.eventmanager.dto.ticket.TicketRequestDto;
import com.nye.university.eventmanager.dto.ticket.TicketResponseDto;
import com.nye.university.eventmanager.entity.Event;
import com.nye.university.eventmanager.entity.Ticket;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.repository.EventRepository;
import com.nye.university.eventmanager.repository.TicketRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** A TicketService egységtesztjei. */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private TicketService ticketService;

  private Event buildEvent() {
    return Event.builder()
        .id(1L)
        .name("Teszt Esemény")
        .location("Budapest")
        .startDateTime(LocalDateTime.of(2026, 6, 15, 18, 0))
        .capacity(100)
        .build();
  }

  private Ticket buildTicket(Event event) {
    return Ticket.builder()
        .id(1L)
        .ownerName("Kovács János")
        .ticketType("VIP")
        .price(BigDecimal.valueOf(5000))
        .event(event)
        .build();
  }

  private TicketRequestDto buildRequest() {
    return TicketRequestDto.builder()
        .ownerName("Kovács János")
        .ticketType("VIP")
        .price(BigDecimal.valueOf(5000))
        .eventId(1L)
        .build();
  }

  @Test
  void createTicketWhenEventExistsThenReturnSavedTicket() {
    // given
    Event event = buildEvent();
    Ticket saved = buildTicket(event);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(ticketRepository.save(any())).thenReturn(saved);

    // when
    TicketResponseDto result = ticketService.createTicket(buildRequest());

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getOwnerName()).isEqualTo("Kovács János");
    assertThat(result.getEventId()).isEqualTo(1L);
    assertThat(result.getEventName()).isEqualTo("Teszt Esemény");
    verify(eventRepository).findById(1L);
    verify(ticketRepository).save(any());
  }

  @Test
  void createTicketWhenEventNotFoundThenThrowException() {
    // given
    when(eventRepository.findById(99L)).thenReturn(Optional.empty());
    TicketRequestDto request = TicketRequestDto.builder()
        .ownerName("Kovács János")
        .ticketType("VIP")
        .price(BigDecimal.valueOf(5000))
        .eventId(99L)
        .build();

    // when + then
    assertThatThrownBy(() -> ticketService.createTicket(request))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(eventRepository).findById(99L);
  }

  @Test
  void getAllTicketsWhenEmptyThenReturnEmptyList() {
    // given
    when(ticketRepository.findAll()).thenReturn(List.of());

    // when
    List<TicketResponseDto> result = ticketService.getAllTickets();

    // then
    assertThat(result).isEmpty();
    verify(ticketRepository).findAll();
  }

  @Test
  void getAllTicketsWhenNotEmptyThenReturnAllTickets() {
    // given
    Event event = buildEvent();
    when(ticketRepository.findAll()).thenReturn(List.of(buildTicket(event)));

    // when
    List<TicketResponseDto> result = ticketService.getAllTickets();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getOwnerName()).isEqualTo("Kovács János");
    verify(ticketRepository).findAll();
  }

  @Test
  void getAllTicketsWhenTicketHasNoEventThenEventFieldsAreNull() {
    // given
    Ticket ticketWithoutEvent = buildTicket(null);
    when(ticketRepository.findAll()).thenReturn(List.of(ticketWithoutEvent));

    // when
    List<TicketResponseDto> result = ticketService.getAllTickets();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getEventId()).isNull();
    assertThat(result.get(0).getEventName()).isNull();
  }

  @Test
  void getTicketByIdWhenTicketExistsThenReturnTicket() {
    // given
    Event event = buildEvent();
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(buildTicket(event)));

    // when
    TicketResponseDto result = ticketService.getTicketById(1L);

    // then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getTicketType()).isEqualTo("VIP");
    verify(ticketRepository).findById(1L);
  }

  @Test
  void getTicketByIdWhenTicketNotFoundThenThrowException() {
    // given
    when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> ticketService.getTicketById(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(ticketRepository).findById(99L);
  }

  @Test
  void updateTicketWhenBothExistThenReturnUpdatedTicket() {
    // given
    Event event = buildEvent();
    Ticket existing = buildTicket(event);
    TicketRequestDto request = TicketRequestDto.builder()
        .ownerName("Nagy Péter")
        .ticketType("Standard")
        .price(BigDecimal.valueOf(2000))
        .eventId(1L)
        .build();
    Ticket updated = Ticket.builder()
        .id(1L)
        .ownerName("Nagy Péter")
        .ticketType("Standard")
        .price(BigDecimal.valueOf(2000))
        .event(event)
        .build();

    when(ticketRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
    when(ticketRepository.save(any())).thenReturn(updated);

    // when
    TicketResponseDto result = ticketService.updateTicket(1L, request);

    // then
    assertThat(result.getOwnerName()).isEqualTo("Nagy Péter");
    assertThat(result.getTicketType()).isEqualTo("Standard");
    verify(ticketRepository).findById(1L);
    verify(eventRepository).findById(1L);
    verify(ticketRepository).save(any());
  }

  @Test
  void updateTicketWhenTicketNotFoundThenThrowException() {
    // given
    when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> ticketService.updateTicket(99L, buildRequest()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(ticketRepository).findById(99L);
  }

  @Test
  void updateTicketWhenEventNotFoundThenThrowException() {
    // given
    Event event = buildEvent();
    when(ticketRepository.findById(1L)).thenReturn(Optional.of(buildTicket(event)));
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> ticketService.updateTicket(1L, buildRequest()))
        .isInstanceOf(ResourceNotFoundException.class);
    verify(ticketRepository).findById(1L);
    verify(eventRepository).findById(1L);
  }

  @Test
  void deleteTicketWhenTicketExistsThenDeleteSuccessfully() {
    // given
    when(ticketRepository.existsById(1L)).thenReturn(true);

    // when
    ticketService.deleteTicket(1L);

    // then
    verify(ticketRepository).existsById(1L);
    verify(ticketRepository).deleteById(1L);
  }

  @Test
  void deleteTicketWhenTicketNotFoundThenThrowException() {
    // given
    when(ticketRepository.existsById(99L)).thenReturn(false);

    // when + then
    assertThatThrownBy(() -> ticketService.deleteTicket(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("99");
    verify(ticketRepository).existsById(99L);
  }

}
