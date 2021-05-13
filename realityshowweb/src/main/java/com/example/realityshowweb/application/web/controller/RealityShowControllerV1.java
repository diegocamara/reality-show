package com.example.realityshowweb.application.web.controller;

import com.example.realityshow.feature.reactive.CreateRealityShow;
import com.example.realityshowweb.application.web.model.CreateRealityShowRequest;
import com.example.realityshowweb.application.web.model.RealityShowResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reality-shows")
public class RealityShowControllerV1 {

  private final CreateRealityShow createRealityShow;

  @PostMapping
  public Mono<ResponseEntity<RealityShowResponse>> createRealityShow(
      @RequestBody CreateRealityShowRequest createRealityShowRequest) {
    return createRealityShow
        .handle(createRealityShowRequest.toCreateRealityShowInputParams())
        .map(
            realityShow ->
                ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RealityShowResponse(realityShow)));
  }
}
