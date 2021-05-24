package com.example.realityshow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateVotingDayInputParams {
  private UUID realityShowId;
  private LocalDate day;
  private LocalDateTime start;
  private LocalDateTime end;
  private List<Participant> participants;
}
