package com.waterlogged.backend.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.waterlogged.backend.backend.model.SearchResultType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import io.micrometer.core.instrument.util.IOUtils;
import static org.assertj.core.api.Assertions.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BackendApplication.class)
public class RiverQueryIntTest {

    // Root folders for request and response test templates
    private static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/request/%s.graphql";
    private static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/response/%s.json";

    @Autowired
    GraphQLTestTemplate graphQLTestTemplate;


    //************************************************************
    // TESTS
    //************************************************************

    /**
     * Tests full valid response of getRiverById request
     */
    @Test
    void validGetRiverByIdTest() throws IOException {

        String requestFile = "getRiverById";
        String responseFile = "validGetRiverById";

        String id = "river-1";

        ObjectNode requestParameters = new ObjectMapper().createObjectNode();
        requestParameters.put("id", id);

        compareResponsesTest(requestFile, responseFile, requestParameters);

    }

    /**
     * Tests river not found error response of getRiverById request caused by invalid Ids
     * @throws IOException
     */
    @Test
    void riverErrorGetRiverByIdTest() throws IOException {

        String requestFile = "getRiverById";
        String responseFile = "riverErrorGetRiverById";

        List<String> ids = List.of( "", "river-XXX" );

        for( String id : ids ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("id", id);
    
            compareResponsesTest(requestFile, responseFile, requestParameters);

        }

    }

    /**
     * Tests full valid response of getRiverByName request
     * @throws IOException
     */
    @Test
    void validGetRiverByNameTest() throws IOException {

        String requestFile = "getRiverByName";
        String responseFile = "validGetRiverByName";

        ObjectNode requestParameters = new ObjectMapper().createObjectNode();
        requestParameters.put("name", "Verdanson");

        compareResponsesTest(requestFile, responseFile, requestParameters);

    }

    /**
     * Tests river not found error response of getRiverByName request caused by invalid names
     * @throws IOException
     */
    @Test
    void riverErrorGetRiverByNameTest() throws IOException {

        String requestFile = "getRiverByName";
        String responseFile = "riverErrorGetRiverByName";

        List<String> names = List.of( "", "river-XXX" );

        for( String name : names ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("name", name);

            compareResponsesTest(requestFile, responseFile, requestParameters);

        }

    }

    /**
     * Tests that the responses of getRiversInArea are all contained in the specified area
     * @throws IOException
     */
    @Test
    void validGetRiversInAreaTest() throws IOException, JSONException {

        String requestFile = "getRiversInArea";
        double targetLat = 44.5;
        double targetLng = 4.0;
        double radius = 50.0;

        ObjectNode requestParameters = new ObjectMapper().createObjectNode();
        requestParameters.put("latitude", targetLat);
        requestParameters.put("longitude", targetLng);
        requestParameters.put("radius", radius);

        GraphQLResponse graphQLResponse  = sendGraphQLRequest(requestFile, requestParameters);
        assertThat(graphQLResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONArray jsonResults = getQraphQLResponseResults(graphQLResponse, requestFile);

        for(int i=0; i<jsonResults.length(); i++) {

            double riverLat = jsonResults.getJSONObject(i).getJSONObject("coordinate").getDouble("latitude");
            double riverLng = jsonResults.getJSONObject(i).getJSONObject("coordinate").getDouble("longitude");

            assertThat( 
                Math.pow(riverLat-targetLat,2) + Math.pow(riverLng-targetLng,2) )
                .isLessThanOrEqualTo( Math.pow(radius, 2) );
        }

    }

    /**
     * Tests river not found error response of getRiversInArea request caused by invalid radii
     * @throws IOException
     */
    @Test
    void riverErrorGetRiversInAreaTest() throws IOException {

        String requestFile = "getRiversInArea";
        String responseFile = "riverErrorGetRiversInArea";

        List<Double> radii = List.of( 0.0, -10.0 );

        for( double radius : radii ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("latitude", 44.5);
            requestParameters.put("longitude", 4.05);
            requestParameters.put("radius", radius);
    
            compareResponsesTest(requestFile, responseFile, requestParameters);

        }

    }

     /**
     * Tests that the number of responses of getRiversAroundAPlace is not null and increasing with radius value
     * @throws IOException
     */
    @Test
    void validGetRiversAroundAPlaceTest() throws IOException {

        String requestFile = "getRiversAroundAPlace";
        String placeName = "Alès";

        double[] validRadii = { 30, 100, 500 };
        int[] nbResults = { 1, 3, 5 };

        for(int i=0; i<validRadii.length; i++) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("name", placeName);
            requestParameters.put("radius", validRadii[i]);

            GraphQLResponse graphQLResponse  = sendGraphQLRequest(requestFile, requestParameters);
            assertThat(graphQLResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    
            JSONArray jsonResults = getQraphQLResponseResults(graphQLResponse, requestFile);
            assertThat(jsonResults.length()).isGreaterThanOrEqualTo(nbResults[i]);
        }

    }

    /**
     * Tests river not found error response of getRiversAroundAPlace request caused by invalid radii
     * @throws IOException
     */
    @Test
    void riverErrorGetRiversAroundAPlaceTest() throws IOException {

        String requestFile = "getRiversAroundAPlace";
        String responseFile = "riverErrorGetRiversAroundAPlace";

        String name = "Alès";
        List<Double>radii = List.of( 0.0, -10.0 );

        for( double radius : radii ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("name", name);
            requestParameters.put("radius", radius);
    
            compareResponsesTest(requestFile, responseFile, requestParameters);
        }
    
    }

    /**
     * Tests place not found error response of getRiversAroundAPlace request caused by invalid place names
     * @throws IOException
     */
    @Test
    void placeErrorGetRiversAroundAPlaceTest() throws IOException {

        String requestFile = "getRiversAroundAPlace";
        String responseFile = "placeErrorGetRiversAroundAPlace";

        List<String> names = List.of( "", "A4X_Cq" );

        for( String name : names ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("name", name);
            requestParameters.put("radius", 100.0);
    
            compareResponsesTest(requestFile, responseFile, requestParameters);

        }

    }

    /**
     * Test that the response of searchFromPattern request send both RIVER and PLACE types
     * and that the results are sorted alphabetically
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void validSearchFromPatternTest() throws IOException, JSONException {

        String requestFile = "searchFromPattern";
        String pattern = "La";

        ObjectNode requestParameters = new ObjectMapper().createObjectNode();
        requestParameters.put("pattern", pattern);

        GraphQLResponse graphQLResponse  = sendGraphQLRequest(requestFile, requestParameters);
        assertThat(graphQLResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONArray jsonResults = getQraphQLResponseResults(graphQLResponse, requestFile);

        boolean isRiver = false;
        boolean isPlace = false;

        for(int i=0; i<jsonResults.length(); i++) {

            String type = jsonResults.getJSONObject(i).getString("type");

            if( type.equals(SearchResultType.RIVER.toString()) ) { isRiver = true; }

            if( type.equals(SearchResultType.PLACE.toString()) ) { isPlace = true; }

            if(i>0) {
                String previousType = jsonResults.getJSONObject(i-1).getString("type");
                assertThat(previousType.compareTo(type)).isGreaterThanOrEqualTo(0);
            }

        }

        assertThat(isRiver && isPlace).isTrue();

    }

    /**
     * Tests place not found error response of searchFromPattern request caused by invalid patterns
     * @throws IOException
     */
    @Test
    void searchErrorSearchFromPatternTest() throws IOException {

        String requestFile = "searchFromPattern";
        String responseFile = "searchErrorSearchFromPattern";

        List<String> patterns = List.of( "", "A4X_Cq" );

        for( String pattern : patterns ) {

            ObjectNode requestParameters = new ObjectMapper().createObjectNode();
            requestParameters.put("pattern", pattern);
    
            compareResponsesTest(requestFile, responseFile, requestParameters);

        }

    }


    //************************************************************
    // PRIVATE FUNCTIONS
    //************************************************************

    /**
     * Reads a json response file
     * @param location name of file to be readed
     * @return String of file content
     * @throws IOException
     */
    private String read(String location) throws IOException {
        return  IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
    }

    /**
     * Sends GraphQL request and compare response with expected responses
     * @param requestFile file name containing GraphQL request
     * @param responseFile file name containing expected response
     * @param requestParameters parameters to pass to the request
     * @throws IOException
     */
    private void compareResponsesTest (
        String requestFile, 
        String responseFile, 
        ObjectNode requestParameters) throws IOException {

        GraphQLResponse graphQLResponse  = sendGraphQLRequest(requestFile, requestParameters);

        String expectedResponse = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responseFile));

        assertThat(graphQLResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals(expectedResponse, graphQLResponse.getRawResponse().getBody(), true);

    }

    /**
     * Sends GraphQL request
     * @param requestFile file name containing GraphQL request
     * @param responseFile file name containing expected response
     * @param requestParameters parameters to pass to the request
     * @return the graphQL response
     * @throws IOException
     */
    private GraphQLResponse sendGraphQLRequest(
        String requestFile, 
        ObjectNode requestParameters) throws IOException {

        return graphQLTestTemplate
            .perform( String.format(GRAPHQL_QUERY_REQUEST_PATH, requestFile), requestParameters );

    }

    /**
     * Parses the GrapQL response to retrieve an array of results
     * Remarks : the response must contain an array
     * @param graphQLResponse : response to parse
     * @param requestName name of request
     * @return JSONArray with results
     */
    private JSONArray getQraphQLResponseResults(GraphQLResponse graphQLResponse, String requestName) {
        JSONObject jsonResponse = new JSONObject(graphQLResponse.getRawResponse().getBody());
        return jsonResponse.getJSONObject("data").getJSONArray(requestName);
    }
}
