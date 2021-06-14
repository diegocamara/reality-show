package com.example.realityshowvotes;

import com.example.realityshowvotes.application.messaging.model.VoteMessage;
import com.example.realityshowvotes.infrastructure.repository.document.VoteDocument;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;

import java.time.LocalDateTime;
import java.util.UUID;

public class UtilsIntegrationTests extends ApplicationContextIntegratedTests {

  protected VoteMessage createVote(
      UUID voteId, LocalDateTime date, UUID participant, UUID votingDay) {
    final var voteMessage = new VoteMessage(voteId, date, participant, votingDay);
    final var message = message(voteMessage);
    batchingRabbitTemplate.convertAndSend(RABBITMQ_VOTES_QUEUE_NAME, message);
    return voteMessage;
  }

  @SneakyThrows
  private Message message(VoteMessage voteMessage) {
    return MessageBuilder.withBody(objectMapper.writeValueAsBytes(voteMessage))
        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
        .setContentEncoding("utf-8")
        .build();
  }

  protected VoteMessage voteMessage(VoteDocument voteDocument) {
    return new VoteMessage(
        voteDocument.getId(),
        voteDocument.getDate(),
        voteDocument.getParticipant(),
        voteDocument.getVotingDay());
  }
}
