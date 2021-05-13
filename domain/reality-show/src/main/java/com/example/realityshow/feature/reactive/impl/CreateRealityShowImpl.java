package com.example.realityshow.feature.reactive.impl;

import com.example.realityshow.feature.reactive.CreateRealityShow;
import com.example.realityshow.model.CreateRealityShowInputParams;
import com.example.realityshow.model.ModelValidator;
import com.example.realityshow.model.RealityShow;
import com.example.realityshow.model.reactive.RealityShowRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.inject.Named;

import static com.example.realityshow.model.RealityShowBuilder.builder;

@Named
@AllArgsConstructor
public class CreateRealityShowImpl implements CreateRealityShow {

  private final ModelValidator modelValidator;
  private final RealityShowRepository realityShowRepository;

  @Override
  public Mono<RealityShow> handle(CreateRealityShowInputParams createRealityShowInputParams) {
    return realityShowMono(Mono.just(createRealityShowInputParams))
        .flatMap(realityShow -> realityShowRepository.save(realityShow).thenReturn(realityShow));
  }

  private Mono<RealityShow> realityShowMono(
      Mono<CreateRealityShowInputParams> realityShowInputParamsMono) {
    return realityShowInputParamsMono.map(
        input -> builder(modelValidator).withName(input.getName()).build());
  }
}
