/**
 * Resolver of river's queries
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.resolver;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.waterlogged.backend.backend.model.River;
import com.waterlogged.backend.backend.service.RiverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RiverQuery implements GraphQLQueryResolver {

    @Autowired
    RiverService riverService;

    public River getRiverById(String id) {
        return riverService.getRiverById(id);
    }

    public River getRiverByName(String riverName) {
        return riverService.getRiverByName(riverName);
    }

    public List<River> getRiversInArea(double latitude, double longitude, double radius) {
        return riverService.getRiversInArea(latitude, longitude, radius);
    }

    public List<River> getRiversAroundAPlace(String placeName, double radius) {
        return riverService.getRiversAroundAPlace(placeName, radius);
    }
    
}
