package com.example.realityshow.feature.reactive;

import com.example.realityshow.model.CreateVotingDayInputParams;
import com.example.realityshow.model.VotingDay;
import reactor.core.publisher.Mono;

public interface CreateVotingDay {
  Mono<VotingDay> handle(CreateVotingDayInputParams createVotingDayInputParams);
}
