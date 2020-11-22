/**
 * Make HTTP request to get location names and river data
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.service;

import com.waterlogged.backend.backend.error.NoPlaceFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class RestService {

    @Value("${GOOGLE_API_KEY}")
    private String googleAPIKey;


    private enum ApiBaseUrl {
        GEO("https://maps.googleapis.com/maps/api/geocode/json?address="), 
        PLACES("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="), 
        RIVER("https://...");
     
        private String baseUrl;
     
        ApiBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
     
        public String getBaseUrl() {
            return baseUrl;
        }
    }

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Get coordinate from location name using Google Geocoding API
     * @param name Location name
     * @return Optional of array of double [latitue,longitude]
     */
    public String getCoordinatesOfPlace(String name) {

        if( name.equals("") )
            throw new NoPlaceFoundException();

        String options = "&key=";
        String url = ApiBaseUrl.GEO.getBaseUrl() + name + options + googleAPIKey;
        return restTemplate.getForObject(url, String.class);
    
    }

    /**
     * Get a list of places matching with pattern
     * Results are filtered by cities contained in France and Switzerland
     * Results are in english
     * @param pattern Pattern used to find matching places
     * @return Optional List of Search objects
     */
    public String getPlacesWithPattern(String pattern) {

        if( pattern.equals("") )
            throw new NoPlaceFoundException();

        String options = "&types=(cities)&components=country:ch|country:fr&language=en&key=";
        String url = ApiBaseUrl.PLACES.getBaseUrl() + pattern +  options + googleAPIKey;
        return restTemplate.getForObject(url, String.class);
    
    }


}