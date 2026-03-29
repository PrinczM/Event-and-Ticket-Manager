package com.nye.university.eventmanager.dto.event;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Esemény adatait tartalmazó válasz-objektum. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {

  private Long id;
  private String name;
  private String location;
  private LocalDateTime startDateTime;
  private Integer capacity;

}
