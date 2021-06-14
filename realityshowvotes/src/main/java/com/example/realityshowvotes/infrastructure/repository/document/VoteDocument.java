package com.example.realityshowvotes.infrastructure.repository.document;

import com.example.realityshowvotes.application.messaging.model.VoteMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document("#{@environment.getProperty('collections.votes')}")
public class VoteDocument {

  @Id private UUID id;
  private LocalDateTime date;
  private UUID participant;
  private UUID votingDay;

  public VoteDocument(VoteMessage voteMessage) {
    this.id = voteMessage.getId();
    this.date = voteMessage.getDate();
    this.participant = voteMessage.getParticipant();
    this.votingDay = voteMessage.getVotingDay();
  }
}
