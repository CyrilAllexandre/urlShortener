package com.notarius.UrlShortener.controller;

import com.notarius.UrlShortener.exception.InvalidUrlException;
import com.notarius.UrlShortener.service.UrlShorteningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShorteningService urlShorteningService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) throws MalformedURLException, InvalidUrlException, NoSuchAlgorithmException {
        return new ResponseEntity<>(urlShorteningService.shortenUrl(longUrl), HttpStatus.OK);
    }

    @PostMapping("/lengthen")
    public ResponseEntity<String> lengthenUrl(@RequestBody String shortUrl) throws InvalidUrlException {
        return urlShorteningService.lengthenUrl(shortUrl) == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(urlShorteningService.lengthenUrl(shortUrl), HttpStatus.OK);
    }
}
