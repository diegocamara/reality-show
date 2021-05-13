package com.example.realityshowweb.infrastructure.repository.springdata.mongodb.reactiverepository;

import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.document.RealityShowDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface ReactiveMongoRealityShowRepository
    extends ReactiveMongoRepository<RealityShowDocument, UUID> {}
