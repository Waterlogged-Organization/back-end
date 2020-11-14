/**
 * Manage parsing of HTTP place requests
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */
package com.waterloggedorganisation.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.waterloggedorganisation.backend.model.Search;
import com.waterloggedorganisation.backend.model.SearchType;

import org.json.*;

import org.springframework.stereotype.Component;

@Component
public class LocationManager {

    /**
     * Parse the HTTP of Google Geocoding API response 
     * @param response response to parse
     * @return Optional of array of latitude, longitude
     */
    public Optional<double[]> parseCoordinatesFromPlace( Optional<String> response ) {

        if( response.isEmpty() ) {
            return Optional.empty();
        }

        try {

            JSONObject jsonObj = new JSONObject(response.get());

            JSONObject location = jsonObj.getJSONArray("results")
                                    .getJSONObject(0)
                                    .getJSONObject("geometry")
                                    .getJSONObject("location");

            double[] coordinates = { location.getDouble("lat"), location.getDouble("lng") };

            return Optional.of(coordinates);

        } catch (Exception e) { return Optional.empty(); }


    }

    /**
     * Parse the HTTP response of Google Place API
     * @param response response to parse
     * @return Optional list of Search objetcs
     */
    public Optional<List<Search>> parsePlacesFromPattern( Optional<String> response ) {
        
        if( response.isEmpty() ) {
            return Optional.empty();
        }

        try {

            JSONObject jsonObj = new JSONObject(response.get());

            JSONArray predictions = jsonObj.getJSONArray("predictions");

            List<Search> searchResults = new ArrayList<>();

            for(int i=0; i<predictions.length(); i++) {
                String name = predictions.getJSONObject(i).getString("description");
                searchResults.add( new Search(name, SearchType.LOCATION) );
            }

            return Optional.of(searchResults);

        } catch (Exception e) { return Optional.empty(); }



    }

    
}
