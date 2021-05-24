package com.example.realityshowweb;

import com.example.realityshowweb.application.web.model.CreateRealityShowRequest;
import com.example.realityshowweb.application.web.model.RealityShowResponse;
import com.example.realityshowweb.application.web.model.VoteRequest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

class RealityShowWebApplicationTestsApplicationContext extends UtilsIntegrationTests {

  private final String REALITY_SHOWS_URL = "/api/v1/reality-shows";

  @Test
  void shouldCreateRealityShowWithSuccess() {

    final var createRealityShowRequest = new CreateRealityShowRequest("Video Games Super Reality");
    final var requestSpecification =
        RestAssured.given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(writeValueAsString(createRealityShowRequest));

    final var createRealityShowResponse = requestSpecification.post(REALITY_SHOWS_URL);

    createRealityShowResponse
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body(
            "id",
            CoreMatchers.notNullValue(),
            "name",
            CoreMatchers.is(createRealityShowRequest.getName()));

    final var realityShowResponse = createRealityShowResponse.body().as(RealityShowResponse.class);

    final var storedRealityShowDocument =
        reactiveMongoRealityShowRepository.findById(realityShowResponse.getId()).block();

    Assertions.assertNotNull(storedRealityShowDocument);
    Assertions.assertEquals(
        createRealityShowRequest.getName(), storedRealityShowDocument.getName());
  }

  @Test
  void givenAnValidRealityShowWhenVoting10TimesShouldCreateABatchOfMessagesInQueueWithSuccess() {
    final var realityShow = createRealityShow("Reality Show 1");
    final var participant1 = createParticipant(realityShow, "Participant 1");
    final var participant2 = createParticipant(realityShow, "Participant 2");
    final var start = LocalDateTime.now();
    final var end = start.plusDays(2);
    final var day = start.toLocalDate();
    final var votingDay = createVotingDay(realityShow, day, start, end, participant1, participant2);

    final var voteRequest = new VoteRequest(participant1.getId());

    final var voteRequestSpecification =
        RestAssured.given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .pathParam("realityShowId", realityShow.getId())
            .pathParam("votingDayId", votingDay.getId())
            .body(writeValueAsString(voteRequest));

    IntStream.range(0, 10)
        .forEach(
            index ->
                voteRequestSpecification.post(
                    REALITY_SHOWS_URL + "/{realityShowId}/votingDays/{votingDayId}"));

    final var votesQueueProperties = rabbitAdmin.getQueueProperties(votesQueue.getName());
    Assertions.assertNotNull(votesQueueProperties);
    final var messageCount =
        Integer.parseInt(votesQueueProperties.get("QUEUE_MESSAGE_COUNT").toString());
    Assertions.assertEquals(1, messageCount);
  }
}
