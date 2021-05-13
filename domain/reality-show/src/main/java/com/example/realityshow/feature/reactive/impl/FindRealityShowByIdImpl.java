package com.example.realityshow.feature.reactive.impl;

import com.example.realityshow.exception.RealityShowNotFoundException;
import com.example.realityshow.feature.reactive.FindRealityShowById;
import com.example.realityshow.model.RealityShow;
import com.example.realityshow.model.reactive.RealityShowRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.inject.Named;
import java.util.UUID;

@Named
@AllArgsConstructor
public class FindRealityShowByIdImpl implements FindRealityShowById {

  private final RealityShowRepository realityShowRepository;

  @Override
  public Mono<RealityShow> handle(UUID id) {
    return realityShowRepository
        .findById(id)
        .switchIfEmpty(Mono.error(RealityShowNotFoundException::new));
  }
}
