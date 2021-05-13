package com.example.realityshow.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

  private final Integer code;
  private final List<String> messages;

  public BusinessException(Integer code, String message) {
    super(message);
    this.code = code;
    this.messages = new LinkedList<>(singletonList(message));
  }

  public BusinessException(Integer code, List<String> messages) {
    super(String.join(", ", messages));
    this.code = code;
    this.messages = messages;
  }

  public List<String> messages() {
    return this.messages;
  }

  public Integer getCode() {
    return code;
  }
}
