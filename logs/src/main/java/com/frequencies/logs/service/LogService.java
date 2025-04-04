package com.frequencies.logs.service;

import com.frequencies.logs.entity.Log;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class LogService {
    private static final Integer MAX_NB_DOCUMENTS = 50;

    @NonNull
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public LogService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    public Log insertLog(@NonNull Log log) {
        return this.mongoTemplate.save(log)
                                 .block();
    }

    @Transactional(readOnly = true)
    public Log findById(@NonNull ObjectId id) {
        return this.mongoTemplate.findById(id, Log.class)
                                 .block();
    }

    @Transactional(readOnly = true)
    public List<Log> findByMessage(@NonNull String message) {
        // search the log containing the specified message
        String message_regex = "/.*" + message + ".*/";

        Query query = Query.query(Criteria.where("message")
                                          .regex(message_regex))
                           .cursorBatchSize(MAX_NB_DOCUMENTS);

        return mongoTemplate.find(query, Log.class)
                            .collectList()
                            .block();
    }

    @Transactional(readOnly = true)
    public List<Log> findByMessageAndDate(@NonNull String message, @NonNull Date dateMin, @NonNull Date dateMax) {
        // search the log containing the specified message
        String message_regex = "/.*" + message + ".*/";
        Criteria criteria = Criteria.where("message")
                                    .regex(message_regex)
                                    .and("date")
                                    .gte(dateMin)
                                    .lte(dateMax);
        Query query = Query.query(criteria)
                           .cursorBatchSize(MAX_NB_DOCUMENTS);
        return mongoTemplate.find(query, Log.class)
                            .collectList()
                            .block();
    }

    @Transactional(readOnly = true)
    public List<Log> findByScopeAndDate(@NonNull String scope, @NonNull LocalDateTime dateMin, @NonNull LocalDateTime dateMax) {
        Criteria criteria = Criteria.where("scope")
                                    .is(scope)
                                    .and("date")
                                    .gte(dateMin)
                                    .lte(dateMax);
        Query query = Query.query(criteria)
                           .cursorBatchSize(MAX_NB_DOCUMENTS);

        return mongoTemplate.find(query, Log.class)
                            .collectList()
                            .block();
    }

    @Transactional(readOnly = true)
    public List<Log> findByAllByDate(@NonNull String dateMin, @NonNull String dateMax) {
        Criteria criteria = Criteria.where("date")
                                    .gte(dateMin)
                                    .lte(dateMax);

        Query query = Query.query(criteria)
                           .cursorBatchSize(MAX_NB_DOCUMENTS);

        return mongoTemplate.find(query, Log.class)
                            .collectList()
                            .block();
    }
}
