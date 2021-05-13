package com.example.realityshow.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
  private UUID id;
  private VotingDay votingDay;
  private Participant participant;
  private LocalDateTime date;
}
