package com.nye.university.eventmanager.dto.ticket;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Jegy adatait tartalmazó válasz-objektum. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {

  private Long id;
  private String ownerName;
  private String ticketType;
  private BigDecimal price;
  private Long eventId;
  private String eventName;

}
