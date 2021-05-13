package com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document;

import com.example.realityshow.model.VotingDay;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class VotingDayDocument {
  private UUID id;
  private LocalDate day;
  private VotingPeriodDocument votingPeriod;
  private List<UUID> participants;

  public VotingDayDocument(VotingDay votingDay) {
    this.id = votingDay.getId();
    this.day = votingDay.getDay();
    this.votingPeriod = new VotingPeriodDocument(votingDay.getVotingPeriod());
    this.participants = new LinkedList<>(votingDay.getParticipants().keySet());
  }
}
