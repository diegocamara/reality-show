package com.example.realityshow.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RealityShowBuilder {

  private final ModelValidator modelValidator;
  private UUID id;
  private String name;
  private final Map<UUID, Participant> participants;
  private final Map<UUID, VotingDay> votingDays;

  private RealityShowBuilder(ModelValidator modelValidator) {
    this.modelValidator = modelValidator;
    this.participants = new HashMap<>();
    this.votingDays = new HashMap<>();
  }

  public static RealityShowBuilder builder(ModelValidator modelValidator) {
    return new RealityShowBuilder(modelValidator);
  }

  public RealityShowBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public RealityShowBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RealityShowBuilder withParticipants(Participant... participants) {
    for (Participant participant : participants) {
      this.participants.put(participant.getId(), participant);
    }
    return this;
  }

  public RealityShowBuilder withVotingDays(VotingDay... votingDays) {
    for (VotingDay votingDay : votingDays) {
      this.votingDays.put(votingDay.getId(), votingDay);
    }
    return this;
  }

  public RealityShow build() {
    final var realityShow = new RealityShow(id(), this.name, this.participants, this.votingDays);
    return modelValidator.validate(realityShow);
  }

  private UUID id() {
    return this.id != null ? this.id : UUID.randomUUID();
  }
}
