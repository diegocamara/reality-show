package com.example.realityshow.exception;

public class VotingDayNotFoundException extends BusinessException {
  public VotingDayNotFoundException() {
    super(3, "voting day not found exception");
  }
}
