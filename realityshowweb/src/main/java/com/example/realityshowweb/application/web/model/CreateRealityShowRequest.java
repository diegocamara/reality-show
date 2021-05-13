package com.example.realityshowweb.application.web.model;

import com.example.realityshow.model.CreateRealityShowInputParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRealityShowRequest {

  private String name;

  public CreateRealityShowInputParams toCreateRealityShowInputParams() {
    return new CreateRealityShowInputParams(this.name);
  }
}
