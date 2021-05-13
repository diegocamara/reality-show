package com.example.realityshow.feature.reactive.impl;

import com.example.realityshow.feature.reactive.CreateParticipant;
import com.example.realityshow.feature.reactive.FindRealityShowById;
import com.example.realityshow.model.CreateParticipantInputParams;
import com.example.realityshow.model.ModelValidator;
import com.example.realityshow.model.Participant;
import com.example.realityshow.model.reactive.RealityShowRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.inject.Named;

import static com.example.realityshow.model.ParticipantBuilder.builder;

@Named
@AllArgsConstructor
public class CreateParticipantImpl implements CreateParticipant {

  private final ModelValidator modelValidator;
  private final FindRealityShowById findRealityShowById;
  private final RealityShowRepository realityShowRepository;

  @Override
  public Mono<Participant> handle(CreateParticipantInputParams createParticipantInputParams) {
    return Mono.just(participant(createParticipantInputParams))
        .flatMap(
            participant ->
                findRealityShowById
                    .handle(createParticipantInputParams.getRealityShowId())
                    .doOnNext(realityShow -> realityShow.addParticipant(participant))
                    .flatMap(realityShowRepository::save)
                    .thenReturn(participant));
  }

  private Participant participant(CreateParticipantInputParams createParticipantInputParams) {
    return builder(modelValidator).withName(createParticipantInputParams.getName()).build();
  }
}
