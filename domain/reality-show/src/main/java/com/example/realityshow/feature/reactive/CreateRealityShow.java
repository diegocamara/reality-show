package com.example.realityshow.feature.reactive;

import com.example.realityshow.model.CreateRealityShowInputParams;
import com.example.realityshow.model.RealityShow;
import reactor.core.publisher.Mono;

public interface CreateRealityShow {
  Mono<RealityShow> handle(CreateRealityShowInputParams createRealityShowInputParams);
}
