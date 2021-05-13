package com.example.realityshow.exception;

public class ParticipantNotFoundInVotingDayException extends BusinessException {
  public ParticipantNotFoundInVotingDayException() {
    super(4, "participant not found in voting day");
  }
}
