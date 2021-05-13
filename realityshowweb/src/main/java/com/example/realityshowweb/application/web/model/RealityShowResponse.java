package com.example.realityshowweb.application.web.model;

import com.example.realityshow.model.RealityShow;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RealityShowResponse {

  private UUID id;
  private String name;

  public RealityShowResponse(RealityShow realityShow) {
    this.id = realityShow.getId();
    this.name = realityShow.getName();
  }
}
