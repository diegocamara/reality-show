package com.example.realityshowvotes;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class RealityShowVotesApplicationTests extends UtilsIntegrationTests {

  @Test
  void shouldPersistVoteMessageBatch() {

    final var participantId = UUID.randomUUID();
    final var votingDay = UUID.randomUUID();

    final var batchSize = 18;

    final var voteMessages =
        IntStream.range(0, batchSize)
            .mapToObj(
                index ->
                    createVote(UUID.randomUUID(), LocalDateTime.now(), participantId, votingDay))
            .collect(Collectors.toList());

    Awaitility.await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> {
              final var resultList =
                  springDataMongoDBVotesRepository.findAll().stream()
                      .map(this::voteMessage)
                      .collect(Collectors.toList());
              Assertions.assertEquals(batchSize, resultList.size());
              Assertions.assertTrue(resultList.containsAll(voteMessages));
            });
  }
}
