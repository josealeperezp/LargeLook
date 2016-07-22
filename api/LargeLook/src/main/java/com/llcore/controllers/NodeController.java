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
import com.llcore.libs.CypherQuery;

import com.llcore.models.Node;

/**
 *
 * @author alejandro
 */
@RestController
public class NodeController extends Neo4jDataSource {
    
    
    //TODO: crete model for this controller
    
    /**
     * Create a new node
     * @param name
     * @param description
     * @param father_node_id
     * @param child_node_id
     * @return 
     */
    @RequestMapping(value = "/node/create", method = RequestMethod.POST)
    public Map<String,Object> createBasicNode(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "father_node_id", required = false) String father_node_id,
            @RequestParam(value = "child_node_id", required = false) String child_node_id) {
        
        Map<String,Object> result;
        UUID randomID = UUID.randomUUID();
        //result = template.queryForMap(CypherQuery.CREATE_NODE_QUERY, randomID.toString(), name, description);
        Node node = new Node();
        result =node.createNode(randomID.toString(), name, description);
        
        if(father_node_id != null) {
            UUID randomID_relationship = UUID.randomUUID();
            result = template.queryForMap(CypherQuery.CREATE_RELATIONSHIP, father_node_id, randomID.toString(),randomID_relationship.toString());
        }
        
        if(child_node_id != null) {
            UUID randomID_relationship = UUID.randomUUID();
            result = template.queryForMap(CypherQuery.CREATE_RELATIONSHIP, randomID.toString(), child_node_id,randomID_relationship.toString());
        }
        
        return result;
    }
    
    /**
     * Delete a node
     * @param id
     * @return 
     */
    @RequestMapping(value = "/node/delete", method = RequestMethod.POST)
    public int deleteBasicNode(
            @RequestParam(value = "id", required = true) String id) {
        int result = template.update(CypherQuery.DELETE_NODE_QUERY, id);
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
        return template.queryForMap(CypherQuery.GET_NODE_QUERY, id);
    }
    
    
}
