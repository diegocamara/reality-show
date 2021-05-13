package com.example.realityshow.model.reactive;

import com.example.realityshow.model.Vote;
import reactor.core.publisher.Mono;

public interface VotesRepository {
  Mono<Void> save(Vote vote);
}
