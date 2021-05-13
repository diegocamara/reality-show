package com.example.realityshowweb.infrastructure.repository;

import com.example.realityshow.model.*;
import com.example.realityshow.model.reactive.RealityShowRepository;
import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document.ParticipantDocument;
import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document.RealityShowDocument;
import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.reactiverepository.ReactiveMongoRealityShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.realityshow.model.RealityShowBuilder.builder;

@Repository
@AllArgsConstructor
public class MongoDBRealityShowRepository implements RealityShowRepository {

  private final ModelValidator modelValidator;
  private final ReactiveMongoRealityShowRepository reactiveMongoRealityShowRepository;

  @Override
  public Mono<Void> save(RealityShow realityShow) {
    return reactiveMongoRealityShowRepository.save(new RealityShowDocument(realityShow)).then();
  }

  @Override
  public Mono<RealityShow> findById(UUID id) {
    return reactiveMongoRealityShowRepository.findById(id).map(this::realityShow);
  }

  private RealityShow realityShow(RealityShowDocument realityShowDocument) {
    return builder(modelValidator)
        .withId(realityShowDocument.getId())
        .withName(realityShowDocument.getName())
        .withParticipants(participants(realityShowDocument.getParticipants()))
        .withVotingDays(votingDays(realityShowDocument))
        .build();
  }

  private Participant[] participants(Map<UUID, ParticipantDocument> participants) {
    return participants.values().stream().map(this::participant).toArray(Participant[]::new);
  }

  private Participant participant(ParticipantDocument participantDocument) {
    return ParticipantBuilder.builder(modelValidator)
        .withId(participantDocument.getId())
        .withName(participantDocument.getName())
        .build();
  }

  private VotingDay[] votingDays(RealityShowDocument realityShowDocument) {
    final var votingDays = realityShowDocument.getVotingDays();
    final var participants = realityShowDocument.getParticipants();
    return votingDays.values().stream()
        .map(
            votingDayDocument ->
                VotingDayBuilder.builder(modelValidator)
                    .withId(votingDayDocument.getId())
                    .withDay(votingDayDocument.getDay())
                    .withPeriod(
                        votingDayDocument.getVotingPeriod().getStart(),
                        votingDayDocument.getVotingPeriod().getEnd())
                    .withParticipants(
                        participants(participants, votingDayDocument.getParticipants()))
                    .build())
        .toArray(VotingDay[]::new);
  }

  private Participant[] participants(
      Map<UUID, ParticipantDocument> allParticipants, List<UUID> participantIds) {
    return participantIds.stream()
        .map(allParticipants::get)
        .map(this::participant)
        .toArray(Participant[]::new);
  }
}
