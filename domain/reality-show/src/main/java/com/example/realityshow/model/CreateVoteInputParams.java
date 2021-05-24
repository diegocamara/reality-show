package com.example.realityshow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateVoteInputParams {
  private UUID realityShowId;
  private UUID votingDayId;
  private UUID participantId;
}
