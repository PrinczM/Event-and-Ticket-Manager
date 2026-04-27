package com.nye.university.eventmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nye.university.eventmanager.dto.ticket.TicketResponseDto;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.service.TicketService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** A TicketController WebMvc-réteg tesztjei. */
@WebMvcTest(TicketController.class)
class TicketControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TicketService ticketService;

  private TicketResponseDto buildResponse() {
    return TicketResponseDto.builder()
        .id(1L)
        .ownerName("Kovács János")
        .ticketType("VIP")
        .price(BigDecimal.valueOf(5000))
        .eventId(1L)
        .eventName("Teszt Esemény")
        .build();
  }

  @Test
  void createTicketWhenRequestIsValidThenReturnCreatedTicket() throws Exception {
    // given
    when(ticketService.createTicket(any())).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(post("/api/tickets")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "ownerName": "Kovács János",
                  "ticketType": "VIP",
                  "price": 5000,
                  "eventId": 1
                }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.ownerName").value("Kovács János"));

    verify(ticketService).createTicket(any());
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void createTicketWhenRequestIsInvalidThenReturnBadRequest() throws Exception {
    // when + then
    mockMvc.perform(post("/api/tickets")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "ownerName": "",
                  "ticketType": ""
                }
                """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllTicketsWhenCalledThenReturnOk() throws Exception {
    // given
    when(ticketService.getAllTickets()).thenReturn(List.of(buildResponse()));

    // when + then
    mockMvc.perform(get("/api/tickets"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].ownerName").value("Kovács János"));

    verify(ticketService).getAllTickets();
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void getTicketByIdWhenTicketExistsThenReturnOk() throws Exception {
    // given
    when(ticketService.getTicketById(1L)).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(get("/api/tickets/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    verify(ticketService).getTicketById(1L);
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void getTicketByIdWhenTicketNotFoundThenReturnNotFound() throws Exception {
    // given
    when(ticketService.getTicketById(99L)).thenThrow(new ResourceNotFoundException("not found"));

    // when + then
    mockMvc.perform(get("/api/tickets/99"))
        .andExpect(status().isNotFound());

    verify(ticketService).getTicketById(99L);
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void updateTicketWhenRequestIsValidThenReturnOk() throws Exception {
    // given
    when(ticketService.updateTicket(eq(1L), any())).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(put("/api/tickets/1")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "ownerName": "Kovács János",
                  "ticketType": "VIP",
                  "price": 5000,
                  "eventId": 1
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ownerName").value("Kovács János"));

    verify(ticketService).updateTicket(eq(1L), any());
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void deleteTicketWhenTicketExistsThenReturnNoContent() throws Exception {
    // given
    doNothing().when(ticketService).deleteTicket(1L);

    // when + then
    mockMvc.perform(delete("/api/tickets/1"))
        .andExpect(status().isNoContent());

    verify(ticketService).deleteTicket(1L);
    verifyNoMoreInteractions(ticketService);
  }

  @Test
  void deleteTicketWhenTicketNotFoundThenReturnNotFound() throws Exception {
    // given
    doThrow(new ResourceNotFoundException("not found")).when(ticketService).deleteTicket(99L);

    // when + then
    mockMvc.perform(delete("/api/tickets/99"))
        .andExpect(status().isNotFound());

    verify(ticketService).deleteTicket(99L);
    verifyNoMoreInteractions(ticketService);
  }

}
