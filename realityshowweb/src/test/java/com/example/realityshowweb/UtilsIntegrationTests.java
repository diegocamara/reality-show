package com.example.realityshowweb;

import com.example.realityshow.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class UtilsIntegrationTests extends ApplicationContextIntegratedTests {

  protected RealityShow createRealityShow(String name) {
    return createRealityShow.handle(new CreateRealityShowInputParams(name)).block();
  }

  protected Participant createParticipant(RealityShow realityShow, String name) {
    return createParticipant
        .handle(new CreateParticipantInputParams(realityShow.getId(), name))
        .block();
  }

  protected VotingDay createVotingDay(
      RealityShow realityShow,
      LocalDate day,
      LocalDateTime start,
      LocalDateTime end,
      Participant... participants) {
    return createVotingDay
        .handle(
            new CreateVotingDayInputParams(
                realityShow.getId(), day, start, end, Arrays.asList(participants)))
        .block();
  }
}
