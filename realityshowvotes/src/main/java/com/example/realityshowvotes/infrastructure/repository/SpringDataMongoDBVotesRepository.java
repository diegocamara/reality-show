package com.example.realityshowvotes.infrastructure.repository;

import com.example.realityshowvotes.infrastructure.repository.document.VoteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataMongoDBVotesRepository extends MongoRepository<VoteDocument, UUID> {}
