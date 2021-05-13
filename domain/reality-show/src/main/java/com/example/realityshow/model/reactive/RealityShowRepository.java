package com.example.realityshow.model.reactive;

import com.example.realityshow.model.RealityShow;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RealityShowRepository {
  Mono<Void> save(RealityShow realityShow);

  Mono<RealityShow> findById(UUID id);
}
