/**
 * Model a Search object
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.model;

public class Search implements Comparable<Search> {

    private String name;
    private SearchResultType type;

    public Search(String name, SearchResultType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchResultType getType() {
        return type;
    }

    public void setType(SearchResultType type) {
        this.type = type;
    }

    @Override
    public int compareTo(Search search) {
        if( getName() == null || getType() == null )
            return 0;
        
        return getName().compareTo(search.getName());
    }

    

    
    
}
