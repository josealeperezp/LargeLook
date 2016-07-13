/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hello;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alejandro
 */
@RestController
public class GetSubjectController {
    
    public static final String NEO4J_URL = System.getProperty("NEO4J_URL","jdbc:neo4j://localhost:7474");
    
    @Autowired
    JdbcTemplate template;
    
    String GET_MOVIE_QUERY =
            "match (n)-[:DISTANCE]-(x) where n.subject = \"<http://street450976>\" return x";
    
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(NEO4J_URL,"neo4j","neo4j");
    }

    
    @RequestMapping("/movie/{title}")
    public Map<String,Object> movie(@PathVariable("title") String title) {
        return template.queryForMap(GET_MOVIE_QUERY, title);
    }
}
