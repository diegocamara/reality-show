package com.example.realityshow.feature.reactive;

import com.example.realityshow.model.CreateParticipantInputParams;
import com.example.realityshow.model.Participant;
import reactor.core.publisher.Mono;

public interface CreateParticipant {
  Mono<Participant> handle(CreateParticipantInputParams createParticipantInputParams);
}
