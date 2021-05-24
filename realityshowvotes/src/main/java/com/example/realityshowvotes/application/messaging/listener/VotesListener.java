package com.example.realityshowvotes.application.messaging.listener;

import com.example.realityshowvotes.application.messaging.model.VoteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class VotesListener {

  @RabbitListener(queues = "#{@environment.getProperty('queue.votes')}")
  public void votes(List<Message<VoteMessage>> votes) {
    votes.forEach(vote -> log.info(vote.getHeaders().get("id", UUID.class).toString()));
  }
}
