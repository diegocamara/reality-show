package com.example.realityshow.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
  @NotNull private UUID id;
  @NotNull @Valid private VotingDay votingDay;
  @NotNull private Participant participant;
  @NotNull private LocalDateTime date;
}
