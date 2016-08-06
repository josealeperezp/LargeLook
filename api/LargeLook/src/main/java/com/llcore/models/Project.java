/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.exceptions.DefaultNeo4jException;
import com.llcore.exceptions.InvalidParamsException;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import com.llcore.exceptions.InvalidUserException;
import com.llcore.libs.CypherQuery;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author alejandro
 */
public class Project {
    public final static String USER = "project";
    public final static String PROJECT = "user";
    
    JdbcTemplate template;
    UUID id;
    UUID user_id;
    String name;
    String type;
    Map<String,Object> full_info;
    List<Map<String,Object>> full_info_list;
    
    /**
     * Constructor
     * @param template 
     */
    public Project(JdbcTemplate template) {
        this.template = template;
        UUID randomID = UUID.randomUUID();
        this.id = randomID;
    }
    
    /**
     * Constructor for existing project (given its ID or user_id)
     * @param template
     * @param id
     * @param type
     * @throws Exception 
     */
    public Project(JdbcTemplate template, UUID id, String type) throws Exception {
        this.type = type;
        this.template = template;
        if(type.equals(PROJECT)) {
            this.find(id);
        } else if(type.equals(USER)) {
            user_id = id;
            this.findByUser(id);
        } else {
            throw new InvalidParamsException("Invalid type param");
        }
        
    }
    
    /**
     * Set user id
     * @param user_id 
     */
    public void setUserId(UUID user_id) {this.user_id = user_id;}
    
    /**
     * Set name
     * @param name 
     */
    public void setName(String name) {this.name = name;}
    
    /**
     * Get user id
     * @return 
     */
    public UUID getUserId() {return this.user_id;}
    
    /**
     * Get name
     * @return 
     */
    public String getName() {return this.name;}
    
    /**
     * Save a project
     * @return
     * @throws Exception 
     */
    public Map<String,Object> save() throws Exception {
        try {
            UUID randomID = UUID.randomUUID();
            return template.queryForMap(CypherQuery.CREATE_PROJECT, id.toString(), name, user_id.toString(),randomID.toString());
        } catch (Exception e) {
            throw new InvalidUserException("Invalid user");
        }
    }
    
    /**
     * Find a project
     * @param id
     * @throws Exception 
     */
    public void find(UUID id) throws Exception {
        try {
            Map<String,Object> node = template.queryForMap(CypherQuery.GET_PROJECT, id.toString());
            this.id = id;
            this.setName((String) node.get("name"));
            this.full_info = node;
        } catch(EmptyResultDataAccessException e) {
                throw new DefaultNeo4jException("Project with id "+id+" doesn't exist");
        }
    }
    
    /**
     * Find projects by user
     * @param user_id
     * @throws Exception 
     */
    public void findByUser(UUID user_id) throws Exception {
        try {
            List<Map<String,Object>> node = template.queryForList(CypherQuery.GET_PROJECT_BY_USER, user_id.toString());
            this.full_info_list = node;
            //return node;
        } catch(EmptyResultDataAccessException e) {
                throw new DefaultNeo4jException("Project for user "+user_id+" doesn't exist");
        }
    }
    
    /**
     * Delete a project
     */
    public void delete() {
        template.update(CypherQuery.DELETE_PROJECT, id.toString());
    }
    
    /**
     * Get info of a project
     * @return 
     */
    public Map<String,Object> getFullInfo() {
        return this.full_info;
    }
    
    /**
     * Get user's projects
     * @return 
     */
    public List<Map<String,Object>> getFullInfoByUser() {
        return this.full_info_list;
    }
    
}
