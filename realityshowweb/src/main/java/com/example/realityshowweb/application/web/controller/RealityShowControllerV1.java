package com.example.realityshowweb.application.web.controller;

import com.example.realityshow.feature.reactive.CreateRealityShow;
import com.example.realityshow.feature.reactive.CreateVote;
import com.example.realityshow.model.CreateVoteInputParams;
import com.example.realityshowweb.application.web.model.CreateRealityShowRequest;
import com.example.realityshowweb.application.web.model.RealityShowResponse;
import com.example.realityshowweb.application.web.model.VoteRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reality-shows")
public class RealityShowControllerV1 {

  private final CreateRealityShow createRealityShow;
  private final CreateVote createVote;

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

  @PostMapping("/{realityShowId}/votingDays/{votingDayId}")
  public Mono<ResponseEntity<Object>> createVote(
      @PathVariable UUID realityShowId,
      @PathVariable UUID votingDayId,
      @RequestBody VoteRequest voteRequest) {
    return createVote
        .handle(new CreateVoteInputParams(realityShowId, votingDayId, voteRequest.getParticipant()))
        .map(vote -> ResponseEntity.status(HttpStatus.CREATED).build())
        .onErrorResume(throwable -> Mono.error(throwable));
  }
}
