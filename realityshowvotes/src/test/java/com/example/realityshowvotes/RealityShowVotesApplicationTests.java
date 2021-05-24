package com.example.realityshowvotes;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

class RealityShowVotesApplicationTests extends UtilsIntegrationTests {

  @Test
  void contextLoads() {

    final var participantId = UUID.randomUUID();

    IntStream.range(0, 10)
        .forEach(
            index -> {
              final var voteMessage =
                  createVote(UUID.randomUUID(), LocalDateTime.now(), participantId);
            });
  }
}
