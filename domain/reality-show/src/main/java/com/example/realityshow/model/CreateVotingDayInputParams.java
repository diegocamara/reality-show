package com.example.realityshow.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateVotingDayInputParams {
  private UUID realityShowId;
  private LocalDate day;
  private LocalDateTime start;
  private LocalDateTime end;
}
