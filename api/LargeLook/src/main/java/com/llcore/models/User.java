/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.exceptions.DefaultNeo4jException;
import com.llcore.libs.CypherQuery;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author alejandro
 */
public class User {
    JdbcTemplate template;
    UUID id;
    String name;
    String lastname;
    String email;
    String password;
    Map<String,Object> user_info;
    
    /**
     * Constructor for User
     * @param template 
     */
    public User(JdbcTemplate template) {
        UUID randomID = UUID.randomUUID();
        this.id = randomID;
        this.template = template;
    }
    
    /**
     * Constructor for existing user
     * @param template
     * @param email
     * @throws Exception 
     */
    public User(JdbcTemplate template, String email) throws Exception {
        this.template = template;
        this.findByEmail(email);
    }
    
    /**
     * Set User name
     * @param name 
     */
    public void setName(String name) {this.name = name;}
    
    /**
     * Set user lastname
     * @param lastname 
     */
    public void setLastname(String lastname) {this.lastname = lastname;}
    
    /**
     * Set email
     * @param email 
     */
    public void setEmail(String email) {this.email = email;}
    
    /**
     * Set user password
     * @param password 
     */
    public void setPassword(String password) {
        byte[] encodedBytes = Base64.encodeBase64(password.getBytes());
        this.password = new String(encodedBytes);
    }
    
    /**
     * Get user name
     * @return 
     */
    public String getName() {return this.name;}
    
    /**
     * Get user lastname
     * @return 
     */
    public String getLastname() {return this.lastname;}
    
    /**
     * Get email
     * @return 
     */
    public String getEmail() {return this.email;}
    
    /**
     * get encrypted password
     * @return 
     */
    public String getPassword() {return this.password;}
    
    /**
     * Save new user
     * @return
     * @throws Exception 
     */
    public Map<String,Object> save() throws Exception {
        try {
            return template.queryForMap(CypherQuery.CREATE_USER, id.toString(), name, lastname, email,password);
        } catch (UncategorizedSQLException e) {
            throw new DefaultNeo4jException("User with email "+email+" already exists");
        }
    }
    
    /**
     * Find user by email
     * @param email
     * @return
     * @throws Exception 
     */
    public Map<String,Object> findByEmail(String email) throws Exception {
        //Map<String,Object> user_info = null;
        try {
            user_info = template.queryForMap(CypherQuery.GET_USER, email);
        } catch (EmptyResultDataAccessException e) {
            throw new DefaultNeo4jException("User with email "+email+" doesn't exist");
        }
        
        this.email = email;
        if(user_info.containsKey("name"))
            this.name =(String) user_info.get("name");
        
        if(user_info.containsKey("lastname"))
            this.name =(String) user_info.get("lastname");
        
        this.id = UUID.fromString((String) user_info.get("id")); //(UUID) user_info.get("id");
        
        return user_info;
    }
    
    /**
     * Delete an user
     * @throws Exception 
     */
    public void delete() throws Exception {
        //String iduser = id.toString();
        template.update(CypherQuery.DELETE_USER_BY_ID, id.toString());
    }
}
