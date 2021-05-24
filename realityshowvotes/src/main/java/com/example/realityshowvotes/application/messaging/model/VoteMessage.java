package com.example.realityshowvotes.application.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteMessage implements Serializable {
  private UUID id;
  private LocalDateTime date;
  private UUID participant;
}
