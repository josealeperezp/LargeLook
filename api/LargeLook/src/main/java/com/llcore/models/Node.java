/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.libs.CypherQuery;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import com.llcore.exceptions.DefaultNeo4jException;

/**
 *
 * @author alejandro
 */
public class Node {
    JdbcTemplate template;
    String name;
    String description;
    UUID id;
    Map<String,Object> full_info;
    
    /**
     * Constructor for new node
     * @param template 
     */
    public Node(JdbcTemplate template){
        this.template = template;
        UUID randomID = UUID.randomUUID();
        this.id = randomID;
    }
    
    /**
     * Constructor for existing node
     * @param template
     * @param id
     * @throws Exception 
     */
    public Node(JdbcTemplate template, UUID id) throws Exception {
        this.template = template;
        this.find(id);
    }
    
    /**
     * Set node name
     * @param name 
     */
    public void setName(String name) {this.name = name;}
    
    /**
     * Get node name
     * @return 
     */
    public String getName() {return name;}
    
    /**
     * Set node description
     * @param description 
     */
    public void setDescription(String description) {this.description = description;}
    
    /**
     * Get node description
     * @return 
     */
    public String getDescription() {return description;}
    
    /**
     * Get node id
     * @return 
     */
    public UUID getId() {return id;}
    
    /**
     * Save node in database
     * @return 
     */
    public Map<String,Object> save() throws Exception {
        return template.queryForMap(CypherQuery.CREATE_NODE_QUERY, this.id.toString(), name, description);
    }
    
    /**
     * Find a node in database
     * @param id
     * @throws Exception 
     */
    public void find(UUID id) throws Exception {
        Map<String,Object> node = template.queryForMap(CypherQuery.GET_NODE_QUERY, id.toString());
        if(node == null)
            throw new DefaultNeo4jException("Node with id "+id+" doesn't exist");
        else {
            this.id = id;
            this.setName((String) node.get("name"));
            this.setDescription((String) node.get("description"));
            this.full_info = node;
        }
    }
    
    /**
     * Delete node from database
     */
    public void delete() {
        template.update(CypherQuery.DELETE_NODE_QUERY, id.toString());
    }
    
    /**
     * Returns all node info
     * @return 
     */
    public Map<String,Object> getAllInfo() {
        return this.full_info;
    }
}
