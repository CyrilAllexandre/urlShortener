package com.notarius.UrlShortener.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urlMappings")
@Data
public class UrlMapping {
    @Id
    private String id;

    private String longUrl;
    private String shortUrl;

}
