package com.yazlab.backend;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MergedWordsRepository extends MongoRepository<MergedWords, ObjectId> {
}
