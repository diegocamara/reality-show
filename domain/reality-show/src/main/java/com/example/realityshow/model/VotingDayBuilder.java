package com.example.realityshow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VotingDayBuilder {

  private final ModelValidator modelValidator;
  private UUID id;
  private LocalDate day;
  private VotingPeriod votingPeriod;
  private final Map<UUID, Participant> participants;

  private VotingDayBuilder(ModelValidator modelValidator) {
    this.modelValidator = modelValidator;
    this.participants = new HashMap<>();
  }

  public static VotingDayBuilder builder(ModelValidator modelValidator) {
    return new VotingDayBuilder(modelValidator);
  }

  public VotingDayBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public VotingDayBuilder withDay(LocalDate day) {
    this.day = day;
    return this;
  }

  public VotingDayBuilder withPeriod(LocalDateTime start, LocalDateTime end) {
    this.votingPeriod = new VotingPeriod(start, end);
    return this;
  }

  public VotingDayBuilder withParticipants(Participant... participants) {
    for (Participant participant : participants) {
      this.participants.put(participant.getId(), participant);
    }
    return this;
  }

  public VotingDay build() {
    final var votingDay = new VotingDay(id(), this.day, this.votingPeriod, this.participants);
    return modelValidator.validate(votingDay);
  }

  private UUID id() {
    return this.id != null ? this.id : UUID.randomUUID();
  }
}
