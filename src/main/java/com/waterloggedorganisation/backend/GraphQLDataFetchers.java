/**
 * Define fake database and route implementations
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */


package com.waterloggedorganisation.backend;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    // Fake database
    private static List<Map<String, String>> rivers = Arrays.asList(

        ImmutableMap.<String, String>builder()
            .put("id", "river-1")
            .put("name", "Valserine")
            .put("level", "55.6")
            .put("difficulty", "3")
            .put("latitude", "46.099998")
            .put("longitude", "5.81667")
            .build(),

        ImmutableMap.<String, String>builder()
            .put("id", "river-2")
            .put("name", "Verdanson")
            .put("level", "55.6")
            .put("difficulty", "3")
            .put("latitude", "43.428443")
            .put("longitude", "5.384198")
            .build(),

        ImmutableMap.<String, String>builder()
            .put("id", "river-3")
            .put("name", "Le Lez")
            .put("level", "55.6")
            .put("difficulty", "3")
            .put("latitude", "43.700001")
            .put("longitude", "3.86667")
            .build(),

        ImmutableMap.<String, String>builder()
            .put("id", "river-4")
            .put("name", "La Cèze")
            .put("level", "55.6")
            .put("difficulty", "3")
            .put("latitude", "44.25645")
            .put("longitude", "4.36892")
            .build(),

        ImmutableMap.<String, String>builder()
            .put("id", "river-5")
            .put("name", "Gardon")
            .put("level", "55.6")
            .put("difficulty", "3")
            .put("latitude", "43.95359")
            .put("longitude", "4.45955")
            .build()

    );

    // DataFetcher of the request riverById
    public DataFetcher getRiverByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String riverId = dataFetchingEnvironment.getArgument("id");
            return rivers
                    .stream()
                    .filter(river -> river.get("id").equals(riverId))
                    .findFirst()
                    .orElse(null);
        };
    }

    // DataFetcher of the request riverByLocation
    public DataFetcher getRiverByLocationDataFetcher() {
        return dataFetchingEnvironment -> {
            Double riverLatitude = dataFetchingEnvironment.getArgument("latitude");
            Double riverLongitude = dataFetchingEnvironment.getArgument("longitude");

            // Radius conversion : 78.567 is equal to 1 degree on earth coordinates
            Double riverRadius = (Double)dataFetchingEnvironment.getArgument("radius") / new Double(78.567);

            return rivers
                    .stream()
                    .filter(river -> 
                        Math.pow(Double.parseDouble(river.get("latitude")) - riverLatitude, 2) + 
                        Math.pow(Double.parseDouble(river.get("longitude")) - riverLongitude, 2) <= riverRadius)
                    .collect(Collectors.toList());

        };
    }

}