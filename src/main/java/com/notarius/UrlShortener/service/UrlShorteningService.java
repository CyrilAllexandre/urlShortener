package com.notarius.UrlShortener.service;

import com.notarius.UrlShortener.exception.InvalidUrlException;
import com.notarius.UrlShortener.model.UrlMapping;
import com.notarius.UrlShortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UrlShorteningService {
    private final UrlMappingRepository urlMappingRepository;
    @Value("${maxshorturllength}")
    private int MAX_SHORT_URL_LENGTH;

    public String shortenUrl(String longUrl) throws InvalidUrlException, MalformedURLException, NoSuchAlgorithmException {
        validateUrl(longUrl);

        UrlMapping existingMapping = urlMappingRepository.findByLongUrl(longUrl);
        if (existingMapping != null) {
            return existingMapping.getShortUrl();
        }

        URL url = new URL(longUrl);
        String domain = getDomain(url);
        String path = url.getPath();
        String shortUrl = generateShortUrl(longUrl, domain, path);
        saveUrlMapping(longUrl, shortUrl);
        return shortUrl;
    }

    public String lengthenUrl(String shortUrl) throws InvalidUrlException {
        validateUrl(shortUrl);
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        return (urlMapping != null) ? urlMapping.getLongUrl() : null;
    }

    public String getDomain(URL url) {
        String protocol = url.getProtocol().concat("://");
        String domain = url.getHost();
        return protocol+domain;
    }

    private String generateShortUrl(String longUrl, String domain, String path) throws NoSuchAlgorithmException {
        if (path.length() == 0) {
            return longUrl;
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(path.getBytes(StandardCharsets.UTF_8));

        int maxLength = Math.min(path.length(), MAX_SHORT_URL_LENGTH);
        String encodedHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, maxLength);

        return domain + "/" + encodedHash;
    }

    private void validateUrl(String url) throws InvalidUrlException {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(url)) {
            throw new InvalidUrlException("The given URL is not a valid one");
        }
    }

    private void saveUrlMapping(String longUrl, String shortUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMappingRepository.save(urlMapping);
    }

}
