package com.example.realityshow.feature.reactive;

import com.example.realityshow.model.RealityShow;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FindRealityShowById {
  Mono<RealityShow> handle(UUID id);
}
