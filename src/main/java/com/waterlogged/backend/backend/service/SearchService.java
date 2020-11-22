/**
 * Manage implementation of search's queries
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.waterlogged.backend.backend.error.NoSearchResultException;
import com.waterlogged.backend.backend.error.NoPlaceFoundException;
import com.waterlogged.backend.backend.error.NoRiverFoundException;
import com.waterlogged.backend.backend.model.Search;
import com.waterlogged.backend.backend.model.SearchResultType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    RestService restService;

    @Autowired
    RiverService riverService;


    /**
     * Search a river, a place or a user absed on a pattern
     * @param pattern Pattern to search 
     * @return List of Search objects or NoSearchResultException if no results
     */
    public List<Search> searchFromPattern(String pattern) {

        List<Search> searchResults = new ArrayList<>();

        if( pattern.equals("") )
            throw new NoSearchResultException();

        // search for rivers in database
        try {
            riverService.getRiversFromPattern(pattern)
                        .forEach(r -> searchResults.add(new Search(r.getName(), SearchResultType.RIVER))
            );
        } catch (NoRiverFoundException e) {}

        // search for places using Google Place API
        try {

            String response = restService.getPlacesWithPattern(pattern);
            JSONObject jsonResponse = new JSONObject(response);
    
            String status = jsonResponse.getString("status");
    
            if( status.equals("OK") ) {
    
                JSONArray predictions = jsonResponse.getJSONArray("predictions");
                
                for(int i=0; i<predictions.length(); i++) {
                    String name = predictions.getJSONObject(i).getString("description");
                    searchResults.add( new Search(name, SearchResultType.PLACE) );
                }
    
            }

        } catch (NoPlaceFoundException e) {}


        if( searchResults.size() == 0 ) throw new NoSearchResultException();

        Collections.sort(searchResults);
        return searchResults;


    }

        
    
}
