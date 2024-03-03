package com.hps.osvscanning.schedule.repository;

import com.hps.osvscanning.model.mongo.MavenEcosystem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MavenRepository extends EcosystemRepository<MavenEcosystem>, MongoRepository<MavenEcosystem, String> {
}
