package com.example.realityshow.feature.reactive.impl;

import com.example.realityshow.feature.reactive.CreateVotingDay;
import com.example.realityshow.feature.reactive.FindRealityShowById;
import com.example.realityshow.model.CreateVotingDayInputParams;
import com.example.realityshow.model.ModelValidator;
import com.example.realityshow.model.VotingDay;
import com.example.realityshow.model.reactive.RealityShowRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.inject.Named;

import static com.example.realityshow.model.VotingDayBuilder.builder;

@Named
@AllArgsConstructor
public class CreateVotingDayImpl implements CreateVotingDay {

  private final ModelValidator modelValidator;
  private final FindRealityShowById findRealityShowById;
  private final RealityShowRepository realityShowRepository;

  @Override
  public Mono<VotingDay> handle(CreateVotingDayInputParams createVotingDayInputParams) {
    return Mono.just(votingDay(createVotingDayInputParams))
        .flatMap(
            votingDay ->
                findRealityShowById
                    .handle(createVotingDayInputParams.getRealityShowId())
                    .doOnNext(realityShow -> realityShow.addVotingDay(votingDay))
                    .flatMap(realityShowRepository::save)
                    .thenReturn(votingDay));
  }

  private VotingDay votingDay(CreateVotingDayInputParams createVotingDayInputParams) {
    return builder(modelValidator)
        .withDay(createVotingDayInputParams.getDay())
        .withPeriod(createVotingDayInputParams.getStart(), createVotingDayInputParams.getEnd())
        .build();
  }
}
