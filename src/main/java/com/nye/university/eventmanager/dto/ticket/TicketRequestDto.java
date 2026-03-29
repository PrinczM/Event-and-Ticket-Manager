package com.nye.university.eventmanager.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Jegy létrehozásához vagy módosításához szükséges kérés-adatok. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {

  @NotBlank(message = "A jegytulajdonos neve kötelező!")
  private String ownerName;

  @NotBlank(message = "A jegytípus megadása kötelező!")
  private String ticketType;

  @NotNull(message = "Az ár megadása kötelező!")
  @DecimalMin(value = "0.0", message = "Az ár nem lehet negatív!")
  private BigDecimal price;

  @NotNull(message = "Az esemény azonosítója kötelező!")
  private Long eventId;

}
