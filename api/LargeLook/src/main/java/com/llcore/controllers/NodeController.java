/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author alejandro
 */
@RestController
public class NodeController extends Neo4jDataSource {
    @Autowired
    JdbcTemplate template;
    
    String CREATE_NODE_QUERY =
            "CREATE (n:Basic { id: {1}, name : {2}, description : {3} }) return n.id as id, n.name as name";
    
    String DELETE_NODE_QUERY =
            "MATCH (n:Basic { id : {1} }) DELETE n";
    
    String GET_NODE_QUERY =
            "MATCH (n:Basic { id : {1} }) RETURN n.id as id, n.name as name, n.description as description";
       
    
    /**
     * Create a new node
     * @param name
     * @param description
     * @return 
     */
    @RequestMapping(value = "/node/create", method = RequestMethod.POST)
    public Map<String,Object> createBasicNode(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "description", required = false) String description) {
        UUID randomID = UUID.randomUUID();
        return template.queryForMap(CREATE_NODE_QUERY, randomID.toString(), name, description);
    }
    
    /**
     * Delete a node
     * @param id
     * @return 
     */
    @RequestMapping(value = "/node/delete", method = RequestMethod.POST)
    public int deleteBasicNode(
            @RequestParam(value = "id", required = true) String id) {
        int result = template.update(DELETE_NODE_QUERY, id);
        return result;
    }
    
    /**
     * Get a node
     * @param id
     * @return 
     */
    @RequestMapping(value = "/node/get", method = RequestMethod.GET)
    public Map<String,Object> getBasicNode(
            @RequestParam(value = "id", required = true) String id) {
        return template.queryForMap(GET_NODE_QUERY, id);
    }
    
    
}
