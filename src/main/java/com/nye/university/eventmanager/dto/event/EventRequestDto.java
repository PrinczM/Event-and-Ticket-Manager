package com.nye.university.eventmanager.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Esemény létrehozásához vagy módosításához szükséges kérés-adatok. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {

  @NotBlank(message = "Az esemény neve kötelező!")
  private String name;

  @NotBlank(message = "Az esemény helyszíne kötelező!")
  private String location;

  @NotNull(message = "Az esemény kezdési időpontja kötelező!")
  private LocalDateTime startDateTime;

  @NotNull(message = "A kapacitás megadása kötelező!")
  @Min(value = 1, message = "A kapacitás legalább 1 kell legyen!")
  private Integer capacity;

}
