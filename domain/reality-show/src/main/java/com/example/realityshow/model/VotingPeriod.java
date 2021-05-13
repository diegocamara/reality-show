package com.example.realityshow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VotingPeriod {
  @NotNull private LocalDateTime start;
  @NotNull private LocalDateTime end;
}
