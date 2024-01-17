package com.notarius.UrlShortener.repository;

import com.notarius.UrlShortener.model.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
    UrlMapping findByShortUrl(String shortUrl);
    UrlMapping findByLongUrl(String shortUrl);
}
