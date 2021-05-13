package com.example.realityshowweb;

import com.example.realityshowweb.application.web.model.CreateRealityShowRequest;
import com.example.realityshowweb.application.web.model.RealityShowResponse;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class RealityShowWebApplicationTests extends IntegratedTests {

  @Test
  void shouldCreateRealityShowWithSuccess() {

    final var createRealityShowRequest = new CreateRealityShowRequest("Video Games Super Reality");
    final var requestSpecification =
        RestAssured.given()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(writeValueAsString(createRealityShowRequest));

    final var url = "/api/v1/reality-shows";

    final var createRealityShowResponse = requestSpecification.post(url);

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
}
