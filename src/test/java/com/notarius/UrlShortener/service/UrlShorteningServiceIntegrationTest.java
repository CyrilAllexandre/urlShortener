package com.notarius.UrlShortener.service;

import com.notarius.UrlShortener.exception.InvalidUrlException;
import com.notarius.UrlShortener.model.UrlMapping;
import com.notarius.UrlShortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UrlShorteningServiceIntegrationTest {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private UrlShorteningService urlShorteningService;

    @Test
    void testShortenCompleteUrlAndRetrieve() throws InvalidUrlException, MalformedURLException, NoSuchAlgorithmException {
        //GIVEN
        String longUrl = "https://www.notarius.com/documents/document1/paragraphes/paragraphe1";
        //WHEN
        String shortUrl = urlShorteningService.shortenUrl(longUrl);
        URL shortUrlResult = new URL(shortUrl);
        String shortUrlDomain = urlShorteningService.getDomain(shortUrlResult) + "/";
        String shortUrlPath = shortUrl.substring(shortUrlDomain.length());
        String retrievedLongUrl = urlShorteningService.lengthenUrl(shortUrl);
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        //THEN
        assertTrue(shortUrlPath.length() <= 10);
        assertEquals(longUrl, retrievedLongUrl);
        assertEquals(longUrl, urlMapping.getLongUrl());
    }

    @Test
    void testShortenUrlWithoutProtocolShouldThrowException() throws InvalidUrlException, MalformedURLException {
        //GIVEN
        String longUrl = "www.notarius.com/documents/document1/paragraphes/paragraphe1";
        //WHEN
        assertThrows(InvalidUrlException.class, () -> {
            String shortUrl = urlShorteningService.shortenUrl(longUrl);
        });
    }

    @Test
    void testShortenUrlWithoutWWWAndRetrieve() throws InvalidUrlException, MalformedURLException, NoSuchAlgorithmException {
        //GIVEN
        String longUrl = "https://notarius.com/documents/document1/paragraphes/paragraphe1";
        //WHEN
        String shortUrl = urlShorteningService.shortenUrl(longUrl);
        URL shortUrlResult = new URL(shortUrl);
        String shortUrlDomain = urlShorteningService.getDomain(shortUrlResult) + "/";
        String shortUrlPath = shortUrl.substring(shortUrlDomain.length());
        String retrievedLongUrl = urlShorteningService.lengthenUrl(shortUrl);
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        //THEN
        assertTrue(shortUrlPath.length() <= 10);
        assertEquals(longUrl, retrievedLongUrl);
        assertEquals(longUrl, urlMapping.getLongUrl());
    }

    @Test
    void testShortenUrlWithoutPathAndRetrieve() throws InvalidUrlException, MalformedURLException, NoSuchAlgorithmException {
        //GIVEN
        String longUrl = "https://notarius.com";
        //WHEN
        String shortUrl = urlShorteningService.shortenUrl(longUrl);
        String retrievedLongUrl = urlShorteningService.lengthenUrl(shortUrl);
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        //THEN
        assertEquals(longUrl, retrievedLongUrl);
        assertEquals(longUrl, urlMapping.getLongUrl());
        assertEquals(longUrl,shortUrl,retrievedLongUrl);
    }

    @Test
    void sameUrlShouldHaveTheSameShortUrl() throws MalformedURLException, InvalidUrlException, NoSuchAlgorithmException {
        //GIVEN
        String url1 = "https://www.notarius.com/documents/document1/paragraphes/paragraphe1";
        String url2 = "https://www.notarius.com/documents/document1/paragraphes/paragraphe1";

        //WHEN
        String shortUrl1 = urlShorteningService.shortenUrl(url1);
        String shortUrl2 = urlShorteningService.shortenUrl(url2);

        //THEN
        assertEquals(shortUrl1,shortUrl2);

    }
}