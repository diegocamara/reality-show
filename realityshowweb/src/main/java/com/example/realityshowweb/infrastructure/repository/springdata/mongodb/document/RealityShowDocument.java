package com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document;

import com.example.realityshow.model.RealityShow;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Document("#{@environment.getProperty('collections.realityshows')}")
public class RealityShowDocument {
  @Id private UUID id;
  private String name;
  private Map<UUID, ParticipantDocument> participants;
  private Map<UUID, VotingDayDocument> votingDays;

  public RealityShowDocument(RealityShow realityShow) {
    this.id = realityShow.getId();
    this.name = realityShow.getName();
    this.participants =
        realityShow.getParticipants().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, entry -> new ParticipantDocument(entry.getValue())));
    this.votingDays =
        realityShow.getVotingDays().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, entry -> new VotingDayDocument(entry.getValue())));
  }
}
