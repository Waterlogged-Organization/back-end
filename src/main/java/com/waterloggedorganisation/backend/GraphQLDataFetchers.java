package com.waterloggedorganisation.backend;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

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
            .put("latitude", "43,428443")
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

}