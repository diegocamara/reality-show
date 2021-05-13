package com.example.realityshow.feature.reactive.impl;

import com.example.realityshow.exception.VotingDayIsNotActiveException;
import com.example.realityshow.feature.reactive.CreateVote;
import com.example.realityshow.feature.reactive.FindRealityShowById;
import com.example.realityshow.model.*;
import com.example.realityshow.model.reactive.VotesRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.inject.Named;
import java.time.LocalDateTime;

@Named
@AllArgsConstructor
public class CreateVoteImpl implements CreateVote {

  private final ModelValidator modelValidator;
  private final FindRealityShowById findRealityShowById;
  private final VotesRepository votesRepository;

  @Override
  public Mono<Vote> handle(CreateVoteInputParams input) {
    return findRealityShowById
        .handle(input.getRealityShowId())
        .map(
            realityShow -> {
              final var votingDay = realityShow.findVotingDayById(input.getVotingDayId());
              if (!votingDay.isActive()) {
                throw new VotingDayIsNotActiveException();
              }
              final var participant = votingDay.findParticipantById(input.getParticipantId());
              return vote(votingDay, participant);
            })
        .flatMap(vote -> votesRepository.save(vote).thenReturn(vote));
  }

  private Vote vote(VotingDay votingDay, Participant participant) {
    return VoteBuilder.builder(modelValidator)
        .withVotingDay(votingDay)
        .withParticipant(participant)
        .withDate(LocalDateTime.now())
        .build();
  }
}
