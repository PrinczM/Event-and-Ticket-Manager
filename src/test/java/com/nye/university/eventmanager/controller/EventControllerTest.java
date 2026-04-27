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

import com.nye.university.eventmanager.dto.event.EventResponseDto;
import com.nye.university.eventmanager.exception.ResourceNotFoundException;
import com.nye.university.eventmanager.service.EventService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Az EventController WebMvc-réteg tesztjei. */
@WebMvcTest(EventController.class)
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EventService eventService;

  private EventResponseDto buildResponse() {
    return EventResponseDto.builder()
        .id(1L)
        .name("Teszt Esemény")
        .location("Budapest")
        .startDateTime(LocalDateTime.of(2026, 6, 15, 18, 0))
        .capacity(100)
        .build();
  }

  @Test
  void createEventWhenRequestIsValidThenReturnCreatedEvent() throws Exception {
    // given
    when(eventService.createEvent(any())).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(post("/api/events")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "name": "Teszt Esemény",
                  "location": "Budapest",
                  "startDateTime": "2026-06-15T18:00:00",
                  "capacity": 100
                }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Teszt Esemény"));

    verify(eventService).createEvent(any());
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void createEventWhenRequestIsInvalidThenReturnBadRequest() throws Exception {
    // when + then
    mockMvc.perform(post("/api/events")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "name": "",
                  "location": ""
                }
                """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllEventsWhenCalledThenReturnOk() throws Exception {
    // given
    when(eventService.getAllEvents()).thenReturn(List.of(buildResponse()));

    // when + then
    mockMvc.perform(get("/api/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Teszt Esemény"));

    verify(eventService).getAllEvents();
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void getEventByIdWhenEventExistsThenReturnOk() throws Exception {
    // given
    when(eventService.getEventById(1L)).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(get("/api/events/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    verify(eventService).getEventById(1L);
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void getEventByIdWhenEventNotFoundThenReturnNotFound() throws Exception {
    // given
    when(eventService.getEventById(99L)).thenThrow(new ResourceNotFoundException("not found"));

    // when + then
    mockMvc.perform(get("/api/events/99"))
        .andExpect(status().isNotFound());

    verify(eventService).getEventById(99L);
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void updateEventWhenRequestIsValidThenReturnOk() throws Exception {
    // given
    when(eventService.updateEvent(eq(1L), any())).thenReturn(buildResponse());

    // when + then
    mockMvc.perform(put("/api/events/1")
            .contentType(APPLICATION_JSON)
            .content("""
                {
                  "name": "Teszt Esemény",
                  "location": "Budapest",
                  "startDateTime": "2026-06-15T18:00:00",
                  "capacity": 100
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Teszt Esemény"));

    verify(eventService).updateEvent(eq(1L), any());
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void deleteEventWhenEventExistsThenReturnNoContent() throws Exception {
    // given
    doNothing().when(eventService).deleteEvent(1L);

    // when + then
    mockMvc.perform(delete("/api/events/1"))
        .andExpect(status().isNoContent());

    verify(eventService).deleteEvent(1L);
    verifyNoMoreInteractions(eventService);
  }

  @Test
  void deleteEventWhenEventNotFoundThenReturnNotFound() throws Exception {
    // given
    doThrow(new ResourceNotFoundException("not found")).when(eventService).deleteEvent(99L);

    // when + then
    mockMvc.perform(delete("/api/events/99"))
        .andExpect(status().isNotFound());

    verify(eventService).deleteEvent(99L);
    verifyNoMoreInteractions(eventService);
  }

}
