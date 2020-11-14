/**
 * Make HTTP request to get location names and river data
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterloggedorganisation.backend.controller;

import java.util.List;
import java.util.Optional;

import com.waterloggedorganisation.backend.model.Search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    @Autowired
    private LocationManager locationManager;

    private String googleAPIKey = "";

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
     * @param name location name
     * @return Optional of array of double [latitue,longitude]
     */
    public Optional<double[]> getCoordinatesFromPlace(String placeName) {
        if( placeName.equals("") ) { return Optional.empty(); }
        String url = ApiBaseUrl.GEO.getBaseUrl() + placeName + "&key=" + googleAPIKey;
        String response = restTemplate.getForObject(url, String.class);
        return locationManager.parseCoordinatesFromPlace( Optional.ofNullable(response) );

    }

    /**
     * Get a list of places matching with pattern
     * Results are filtered by cities contained in France and Switzerland
     * Results are in english
     * @param pattern pattern used to find matching places
     * @return Optional List of Search objects
     */
    public Optional<List<Search>> getPlacesFromPattern(String pattern) {
        if( pattern.equals("") ) { return Optional.empty(); }
        String options = "&types=(cities)&components=country:ch|country:fr&language=en&key=";
        String url = ApiBaseUrl.PLACES.getBaseUrl() + pattern +  options + googleAPIKey;
        String response = this.restTemplate.getForObject(url, String.class);
        return locationManager.parsePlacesFromPattern(Optional.ofNullable(response));
    }



}
