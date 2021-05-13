package com.example.realityshow.model;

import com.example.realityshow.exception.VotingDayNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RealityShow {
  @NotNull private final UUID id;
  @NotBlank private String name;

  @NotNull private Map<UUID, Participant> participants;

  @NotNull private Map<UUID, VotingDay> votingDays;

  public void addParticipant(Participant participant) {
    this.participants.put(participant.getId(), participant);
  }

  public void addVotingDay(VotingDay votingDay) {
    this.votingDays.put(votingDay.getId(), votingDay);
  }

  public VotingDay findVotingDayById(UUID votingDayId) {
    return Optional.ofNullable(this.votingDays.get(votingDayId))
        .orElseThrow(VotingDayNotFoundException::new);
  }
}
