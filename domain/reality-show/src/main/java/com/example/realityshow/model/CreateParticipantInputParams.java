package com.example.realityshow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateParticipantInputParams {
  private UUID realityShowId;
  private String name;
}
