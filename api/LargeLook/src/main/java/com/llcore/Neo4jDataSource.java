/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.llcore.utils.LlProperty;

/**
 *
 * @author alejandro
 * THIS CLASS IS JUST FOR TESTING THE CONNECTION WITH NEO4J
 */
//@RestController
public class Neo4jDataSource {
    
    public LlProperty prop;
    private static String neo4j_url;
    public static String NEO4J_URL;
    
    public Neo4jDataSource() {
        try {
            prop = new LlProperty();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        neo4j_url = "jdbc:neo4j://"+prop.getProperty("neo4j_host")+":7474";
        NEO4J_URL = System.getProperty("NEO4J_URL",neo4j_url);
    }
    
    //public static final String NEO4J_URL = System.getProperty("NEO4J_URL",neo4j_url);
       
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(NEO4J_URL,"neo4j","neo4j");
    }
    
    @Autowired
    public JdbcTemplate template;

}
