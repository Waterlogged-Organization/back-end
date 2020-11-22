package com.waterlogged.backend.backend.error;

import java.util.List;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class NoPlaceFoundException extends RuntimeException implements GraphQLError {

    public NoPlaceFoundException() {
        super("No place found");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }
 
    @Override
    public ErrorClassification getErrorType() {
        return null;
    }

    
}
