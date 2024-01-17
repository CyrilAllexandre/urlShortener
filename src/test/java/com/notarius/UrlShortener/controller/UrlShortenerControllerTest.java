package com.notarius.UrlShortener.controller;

import com.notarius.UrlShortener.exception.InvalidUrlException;
import com.notarius.UrlShortener.service.UrlShorteningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.MalformedURLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UrlShortenerController.class)
class UrlShortenerControllerTest {

    @MockBean
    private UrlShorteningService urlShorteningService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shortenUrl_Post_ValidUrl_ShouldReturnShortenedUrl() throws Exception {
        // GIVEN
        String longUrl = "https://www.notarius.com/tata/toto";
        String shortenedUrl = "https://www.notarius.com/tutu";

        // WHEN
        when(urlShorteningService.shortenUrl(anyString())).thenReturn(shortenedUrl);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(longUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(shortenedUrl));
    }

    @Test
    void shortenUrl_Post_InvalidUrl_ShouldReturnBadRequest() throws Exception {
        // GIVEN
        String invalidUrl = "InvalidUrl$";
        when(urlShorteningService.shortenUrl(anyString())).thenThrow(new MalformedURLException("Invalid URL"));

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUrl))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void lengthenUrl_Post_ValidUrl_ShouldReturnLengthenUrl() throws Exception {
        // GIVEN
        String longUrl = "https://www.notarius.com/tata/toto";
        String shortenedUrl = "https://www.notarius.com/tutu";

        // WHEN
        when(urlShorteningService.lengthenUrl(anyString())).thenReturn(longUrl);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lengthen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shortenedUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(longUrl));
    }

    @Test
    void lengthenUrl_Post_InvalidUrl_ShouldReturnBadRequest() throws Exception {
        // GIVEN
        String invalidUrl = "InvalidUrl$";
        when(urlShorteningService.lengthenUrl(anyString())).thenThrow(new InvalidUrlException("Invalid URL"));

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lengthen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUrl))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
