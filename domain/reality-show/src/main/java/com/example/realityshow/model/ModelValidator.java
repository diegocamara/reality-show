package com.example.realityshow.model;

import com.example.realityshow.exception.ModelValidationException;
import lombok.AllArgsConstructor;

import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Named
@AllArgsConstructor
public class ModelValidator {

  private final Validator validator;

  public <T> T validate(T model) {
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(model);
    if (!constraintViolations.isEmpty()) {
      final var messages =
          constraintViolations.stream()
              .map(
                  constraintViolation ->
                      constraintViolation
                          .getPropertyPath()
                          .toString()
                          .concat(" : ")
                          .concat(constraintViolation.getMessage()))
              .collect(Collectors.toList());
      throw new ModelValidationException(messages);
    }
    return model;
  }
}
