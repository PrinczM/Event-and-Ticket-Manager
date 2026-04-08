package com.nye.university.eventmanager.controller;

import com.nye.university.eventmanager.dto.event.EventRequestDto;
import com.nye.university.eventmanager.dto.event.EventResponseDto;
import com.nye.university.eventmanager.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Az eseménykezelés REST végpontjait tartalmazó controller. */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @PostMapping
  public ResponseEntity<EventResponseDto> createEvent(@RequestBody @Valid EventRequestDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDto>> getAllEvents() {
    return ResponseEntity.ok(eventService.getAllEvents());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
    return ResponseEntity.ok(eventService.getEventById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDto> updateEvent(
      @PathVariable Long id,
      @RequestBody @Valid EventRequestDto request) {
    return ResponseEntity.ok(eventService.updateEvent(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent().build();
  }

}
