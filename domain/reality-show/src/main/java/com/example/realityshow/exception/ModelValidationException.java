package com.example.realityshow.exception;

import java.util.List;

public class ModelValidationException extends BusinessException {

  public ModelValidationException(List<String> messages) {
    super(1, messages);
  }
}
