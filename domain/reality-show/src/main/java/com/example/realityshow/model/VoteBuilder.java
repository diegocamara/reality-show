package com.example.realityshow.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class VoteBuilder {

  private final ModelValidator modelValidator;
  private UUID id;
  private VotingDay votingDay;
  private Participant participant;
  private LocalDateTime date;

  private VoteBuilder(ModelValidator modelValidator) {
    this.modelValidator = modelValidator;
  }

  public static VoteBuilder builder(ModelValidator modelValidator) {
    return new VoteBuilder(modelValidator);
  }

  public VoteBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public VoteBuilder withVotingDay(VotingDay votingDay) {
    this.votingDay = votingDay;
    return this;
  }

  public VoteBuilder withParticipant(Participant participant) {
    this.participant = participant;
    return this;
  }

  public VoteBuilder withDate(LocalDateTime date) {
    this.date = date;
    return this;
  }

  public Vote build() {
    return modelValidator.validate(new Vote(id(), this.votingDay, this.participant, this.date));
  }

  private UUID id() {
    return this.id != null ? this.id : UUID.randomUUID();
  }
}
