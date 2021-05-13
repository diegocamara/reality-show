package com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document;

import com.example.realityshow.model.VotingPeriod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VotingPeriodDocument {
  private LocalDateTime start;
  private LocalDateTime end;

  public VotingPeriodDocument(VotingPeriod votingPeriod) {
    this.start = votingPeriod.getStart();
    this.end = votingPeriod.getEnd();
  }
}
