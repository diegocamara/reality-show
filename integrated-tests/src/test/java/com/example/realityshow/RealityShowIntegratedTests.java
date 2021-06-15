package com.example.realityshow;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.awaitility.Awaitility;
import org.bson.Document;
import org.eclipse.jetty.http.MimeTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.JmeterDsl;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;

public class RealityShowIntegratedTests extends IntegratedTest {

  @Test
  @SneakyThrows
  void mustCreate1MillionVotesInAMaximum5Minutes() {

    createRealityShow();
    final var realityShowId = "6b0bf747-7e65-4131-988d-9bb84e7ff1a8";
    final var votingDayId = "76cb72ba-69f7-422f-851d-623dd2c89af3";
    final var voteRequest = new Document("participant", "6dfedd3c-aae9-4bde-a398-1a1fcf83508d");

    final var targetUrl =
        REALITY_SHOW_WEB_API_URL
            .concat("/")
            .concat(realityShowId)
            .concat("/votingDays/")
            .concat(votingDayId);

    final var threads = 10;
    final var iterations = 100000;

    testPlan(
            JmeterDsl.threadGroup(
                threads,
                iterations,
                JmeterDsl.httpSampler(targetUrl)
                    .post(voteRequest.toJson(), MimeTypes.Type.APPLICATION_JSON_UTF_8)))
        .run();

    Awaitility.await()
        .atMost(Duration.ofMinutes(5))
        .untilAsserted(
            () -> Assertions.assertEquals(threads * iterations, votesCollection.countDocuments()));
  }

  @SneakyThrows
  private void createRealityShow() {
    final var realityShowInputStream =
        RealityShowIntegratedTests.class.getResourceAsStream("/json/realityshow.json");
    assert realityShowInputStream != null;
    final var realityShowStr = IOUtils.toString(realityShowInputStream, StandardCharsets.UTF_8);
    final var realityShowDocument = Document.parse(realityShowStr);
    realityShowsCollection.insertOne(realityShowDocument);
  }
}
