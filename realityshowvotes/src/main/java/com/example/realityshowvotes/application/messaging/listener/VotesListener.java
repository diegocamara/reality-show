package com.example.realityshowvotes.application.messaging.listener;

import com.example.realityshowvotes.application.messaging.model.VoteMessage;
import com.example.realityshowvotes.infrastructure.repository.SpringDataMongoDBVotesRepository;
import com.example.realityshowvotes.infrastructure.repository.document.VoteDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class VotesListener {

  private final SpringDataMongoDBVotesRepository springDataMongoDBVotesRepository;

  @RabbitListener(queues = "#{@environment.getProperty('queue.votes')}", concurrency = "20")
  public void votes(List<VoteMessage> votes, @Header("batch_id") UUID batchId) {
    final var votesDocumentList =
        votes.stream().map(VoteDocument::new).collect(Collectors.toList());
    springDataMongoDBVotesRepository.saveAll(votesDocumentList);
  }
}
