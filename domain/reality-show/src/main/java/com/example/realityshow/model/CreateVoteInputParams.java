package com.example.realityshow.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateVoteInputParams {
  private UUID realityShowId;
  private UUID participantId;
  private UUID votingDayId;
}
