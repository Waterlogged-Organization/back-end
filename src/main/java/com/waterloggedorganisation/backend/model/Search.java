package com.waterloggedorganisation.backend.model;

public class Search {

    private String name;
    private SearchType type;

    public Search(){};

    public Search( String name, SearchType type ) {
        this.name = name;
        this.type = type;
    }


    
}
