/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.llcore.libs.CypherQuery;

/**
 *
 * @author alejandro
 */
@RestController
public class RelationshipController extends Neo4jDataSource {
    @Autowired
    JdbcTemplate template;
    
    //TODO: crete model for this controller
       
    /**
     * Create a new relationship
     * @param start_node_id
     * @param end_node_id
     * @return 
     */
    @RequestMapping(value = "/relationship/create", method = RequestMethod.POST)
    public Map<String,Object> createRelationship(   
            @RequestParam(value = "start_node_id", required = true) String start_node_id, 
            @RequestParam(value = "end_node_id", required = true) String end_node_id) {
        UUID randomID = UUID.randomUUID();
        return template.queryForMap(CypherQuery.CREATE_RELATIONSHIP, start_node_id, end_node_id,randomID.toString());
    }
    
    /**
     * Delete a relationship
     * @param start_node_id
     * @param end_node_id
     * @param relationship_id
     * @return 
     */
    @RequestMapping(value = "/relationship/delete", method = RequestMethod.POST)
    public int deleteRelationship(
            @RequestParam(value = "start_node_id", required = true) String start_node_id, 
            @RequestParam(value = "end_node_id", required = true) String end_node_id,
            @RequestParam(value = "relationship_id", required = false) String relationship_id) {
        if(relationship_id != null)
            return template.update(CypherQuery.DELETE_RELATIONSHIP_BY_ID, start_node_id, relationship_id, end_node_id);
        return template.update(CypherQuery.DELETE_RELATIONSHIP, start_node_id, end_node_id);
    }
}
