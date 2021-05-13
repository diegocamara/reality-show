package com.example.realityshowweb.infrastructure.repository;

import com.example.realityshow.model.Vote;
import com.example.realityshow.model.reactive.VotesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class RabbitMQVotesRepository implements VotesRepository {
  @Override
  public Mono<Void> save(Vote vote) {
    return null;
  }
}
