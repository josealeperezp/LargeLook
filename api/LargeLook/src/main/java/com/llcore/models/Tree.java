/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.exceptions.InvalidProjectException;
import com.llcore.exceptions.InvalidUserException;
import com.llcore.libs.CypherQuery;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author alejandro
 */
public class Tree {
    JdbcTemplate template;
    UUID id;
    UUID project_id;
    String name;
    String type;
    Map<String,Object> full_info;
    List<Map<String,Object>> full_info_list;
    
    /**
     * Constructor
     * @param template 
     */
    public Tree(JdbcTemplate template) {
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
    public Tree(JdbcTemplate template, UUID id) throws Exception {
        this.template = template;
        this.id = id;
        this.find(id);
    }
    
    /**
     * Set user id
     * @param user_id 
     */
    public void setProjectId(UUID project_id) {this.project_id = project_id;}
    
    /**
     * Set name
     * @param name 
     */
    public void setName(String name) {this.name = name;}
    
    /**
     * Get user id
     * @return 
     */
    public UUID getProjectId() {return this.project_id;}
    
    /**
     * Get name
     * @return 
     */
    public String getName() {return this.name;}
    
    /**
     * Save a empty tree
     * @return
     * @throws Exception 
     */
    public Map<String,Object> save() throws Exception {
        try {
            UUID randomID = UUID.randomUUID();
            return template.queryForMap(CypherQuery.CREATE_TREE, id.toString(), name, project_id.toString(),randomID.toString());
        } catch (Exception e) {
            throw new InvalidProjectException("Invalid project");
        }
    }
    
    /**
     * Find a tree by node
     * @param id
     * @throws Exception 
     */
    public void find(UUID id) throws Exception {
        List<Map<String,Object>> results = template.queryForList(CypherQuery.GET_TREE, id.toString());
        this.full_info_list = results;
    }
    
    /**
     * Returns tree info
     * @return 
     */
    public List<Map<String,Object>> getTree() {
        return this.full_info_list;
    }
    
    /**
     * Delete a tree
     * @param id
     * @return
     * @throws Exception 
     */
    public int delete(UUID id) throws Exception {
        Map<String,Object> result = template.queryForMap(CypherQuery.DELETE_TREE, id.toString());
        return (Integer) result.get("n");
    }
    
    /**
     * delete a tree node
     * @param id
     * @return
     * @throws Exception 
     */
    public int deleteNode(UUID id) throws Exception { 
        Map<String,Object> result = template.queryForMap(CypherQuery.DELETE_TREE_NODE, id.toString());
        return (Integer) result.get("n");
    }
}
