package com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document;

import com.example.realityshow.model.Participant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ParticipantDocument {
  private UUID id;
  private String name;

  public ParticipantDocument(Participant participant) {
    this.id = participant.getId();
    this.name = participant.getName();
  }
}
