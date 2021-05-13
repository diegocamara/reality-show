package com.example.realityshow.model;

import com.example.realityshow.exception.ParticipantNotFoundInVotingDayException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingDay {
  @NotNull private UUID id;
  @NotNull private LocalDate day;
  @NotNull @Valid private VotingPeriod votingPeriod;

  @Size(min = 2)
  private Map<UUID, Participant> participants;

  public boolean isActive() {
    final var now = LocalDateTime.now();
    final var start = votingPeriod.getStart();
    final var end = votingPeriod.getEnd();
    return now.isAfter(start) || now.isEqual(start) && now.isBefore(end) || now.isEqual(end);
  }

  public Participant findParticipantById(UUID participantId) {
    return Optional.ofNullable(this.participants.get(participantId))
        .orElseThrow(ParticipantNotFoundInVotingDayException::new);
  }
}
