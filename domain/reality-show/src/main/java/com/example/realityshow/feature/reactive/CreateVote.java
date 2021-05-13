package com.example.realityshow.feature.reactive;

import com.example.realityshow.model.CreateVoteInputParams;
import com.example.realityshow.model.Vote;
import reactor.core.publisher.Mono;

public interface CreateVote {
  Mono<Vote> handle(CreateVoteInputParams input);
}
