package com.nye.university.eventmanager.controller;

import com.nye.university.eventmanager.dto.ticket.TicketRequestDto;
import com.nye.university.eventmanager.dto.ticket.TicketResponseDto;
import com.nye.university.eventmanager.service.TicketService;
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

/** A jegykezelés REST végpontjait tartalmazó controller. */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

  private final TicketService ticketService;

  @PostMapping
  public ResponseEntity<TicketResponseDto> createTicket(
      @RequestBody @Valid TicketRequestDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request));
  }

  @GetMapping
  public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
    return ResponseEntity.ok(ticketService.getAllTickets());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.getTicketById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TicketResponseDto> updateTicket(
      @PathVariable Long id,
      @RequestBody @Valid TicketRequestDto request) {
    return ResponseEntity.ok(ticketService.updateTicket(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
    ticketService.deleteTicket(id);
    return ResponseEntity.noContent().build();
  }

}
