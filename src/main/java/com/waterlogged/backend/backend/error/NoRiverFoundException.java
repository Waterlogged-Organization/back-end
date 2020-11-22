package com.waterlogged.backend.backend.error;

import java.util.List;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class NoRiverFoundException extends RuntimeException implements GraphQLError { 

    public NoRiverFoundException() {
        super("No river found");
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
