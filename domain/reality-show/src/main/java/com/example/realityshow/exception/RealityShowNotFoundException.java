package com.example.realityshow.exception;

public class RealityShowNotFoundException extends BusinessException {
  public RealityShowNotFoundException() {
    super(2, "reality show not found");
  }
}
