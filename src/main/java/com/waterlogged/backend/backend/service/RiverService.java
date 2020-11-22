/**
 * Manage implementation of river's queries
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.waterlogged.backend.backend.error.NoPlaceFoundException;
import com.waterlogged.backend.backend.error.NoRiverFoundException;
import com.waterlogged.backend.backend.model.Coordinate;
import com.waterlogged.backend.backend.model.River;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiverService {

    @Autowired
    RestService restService;

    /**
     * Fake database : 
     * TODO : the presence of emphasis on the names of the rivers induces the absence of the final brace of json's answers
     */
    List<River> riversFakeDb = Arrays.asList(
        new River( "river-1", "Valserine", 55.4, 3, new Coordinate(46.099998, 5.81667) ),
        new River( "river-2", "Verdanson", 72.5, 4, new Coordinate(43.428443, 5.384198) ),
        new River( "river-3", "Le Lez", 120.7, 2, new Coordinate(43.700001, 3.86667) ),
        new River( "river-4", "La Ceze", 190.1, 1, new Coordinate(44.25645, 4.36892) ),
        new River( "river-5", "Le Gardon", 55.6, 3, new Coordinate(43.95359, 4.45955) )
    );

    /**
     * Get a river by id
     * @param id Id to search
     * @return River with matching id or RiverNotFoundException
     */
    public River getRiverById(String id) {

        Optional<River> river = riversFakeDb.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if( river.isPresent() ) { return river.get(); }
        throw new NoRiverFoundException();
    }

    /**
     * Get river with a specific name
     * @param name Name to search
     * @return First river found with matching name or RiverNotFoundException
     */
    public River getRiverByName(String name) {

        Optional<River> river = riversFakeDb.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();

        if( river.isPresent() ) { return river.get(); }
        throw new NoRiverFoundException();
    }

    /**
     * Get rivers with the same pattern contained in their name
     * @param pattern Pattern to search
     * @return List of river object or RiverNotFoundException if no pattern is contained with river name
     */
    public List<River> getRiversFromPattern(String pattern) {

        List<River> rivers = riversFakeDb.stream()
                .filter(r -> r.getName().contains(pattern))
                .collect(Collectors.toList());

        if( rivers.size() == 0 ) 
            throw new NoRiverFoundException();
        
        return rivers;
                
    }

    /**
     * Get rivers in a circle area
     * @param latitude Latitude of the center of the area
     * @param longitude Longitude of the center of the area
     * @param radius Radius in kilometer of the area
     * @return List of river object or RiverNotFoundException if no river is present in the area
     */
    public List<River> getRiversInArea(Double latitude, double longitude, double radius) {

        double riverRadius = radius / 78.567;

        List<River> rivers = riversFakeDb.stream()
                .filter(r -> 
                        Math.pow(r.getCoordinate().getLatitude() - latitude, 2) + 
                        Math.pow(r.getCoordinate().getLongitude() - longitude, 2) <= Math.pow(riverRadius, 2) )
                .collect(Collectors.toList());
        
        if( rivers.size() == 0 ) 
            throw new NoRiverFoundException();

        return rivers; 

    }

    /**
     * Get rivers around a specific place using Google Geocoding API
     * @param name Place name to search around to
     * @param radius Radius of search area around the place
     * @return List of River objects or :
     *      - PlaceNotFoundException if place is not found
     *      - RiverNotFoundException if no river is present in the area
     */
    public List<River> getRiversAroundAPlace(String name, double radius) {

        String response = restService.getCoordinatesOfPlace(name);
        JSONObject jsonResponse = new JSONObject(response);

        String status = jsonResponse.getString("status");

        if(!status.equals("OK"))
            throw new NoPlaceFoundException();
        
        JSONObject location = jsonResponse.getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        return getRiversInArea(
                location.getDouble("lat"),  
                location.getDouble("lng"), 
                radius);

    }
    
}