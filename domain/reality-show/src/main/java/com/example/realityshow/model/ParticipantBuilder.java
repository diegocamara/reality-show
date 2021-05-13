package com.example.realityshow.model;

import java.util.UUID;

public class ParticipantBuilder {

  private final ModelValidator modelValidator;
  private UUID id;
  private String name;

  private ParticipantBuilder(ModelValidator modelValidator) {
    this.modelValidator = modelValidator;
  }

  public ParticipantBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ParticipantBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public static ParticipantBuilder builder(ModelValidator modelValidator) {
    return new ParticipantBuilder(modelValidator);
  }

  public Participant build() {
    return modelValidator.validate(new Participant(id(), this.name));
  }

  private UUID id() {
    return this.id != null ? this.id : UUID.randomUUID();
  }
}
