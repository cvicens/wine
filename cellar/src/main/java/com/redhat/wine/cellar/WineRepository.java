package com.redhat.wine.cellar;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WineRepository extends MongoRepository<Wine, String> {

    public List<Wine>  findByType(WineType type);
    public List<Wine>  findByTypeAndRegion(WineType type, String region);

}
