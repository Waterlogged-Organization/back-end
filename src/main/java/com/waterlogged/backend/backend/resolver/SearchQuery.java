/**
 * Resolver of search's queries
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterlogged.backend.backend.resolver;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.waterlogged.backend.backend.model.Search;
import com.waterlogged.backend.backend.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SearchQuery implements GraphQLQueryResolver {

    @Autowired
    SearchService searchService;

    public List<Search> searchFromPattern(String pattern) {
        return searchService.searchFromPattern(pattern);
    }

    
}
