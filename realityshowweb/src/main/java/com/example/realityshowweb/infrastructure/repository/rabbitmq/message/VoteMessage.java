package com.example.realityshowweb.infrastructure.repository.rabbitmq.message;

import com.example.realityshow.model.Vote;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VoteMessage implements Serializable {

  private UUID id;
  private LocalDateTime date;
  private UUID participant;

  public VoteMessage(Vote vote) {
    this.id = vote.getId();
    this.date = vote.getDate();
    this.participant = vote.getParticipant().getId();
  }
}
