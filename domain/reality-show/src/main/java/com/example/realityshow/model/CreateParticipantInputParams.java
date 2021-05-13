package com.example.realityshow.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateParticipantInputParams {
  private UUID realityShowId;
  private String name;
}
