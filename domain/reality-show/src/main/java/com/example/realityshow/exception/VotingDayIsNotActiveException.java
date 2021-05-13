package com.example.realityshow.exception;

public class VotingDayIsNotActiveException extends BusinessException {
  public VotingDayIsNotActiveException() {
    super(5, "voting day is not active");
  }
}
