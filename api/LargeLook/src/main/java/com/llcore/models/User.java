/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.libs.CypherQuery;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
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
    
    public User(JdbcTemplate template) {
        UUID randomID = UUID.randomUUID();
        this.id = randomID;
        this.template = template;
    }
    
    public User(JdbcTemplate template, String email) throws Exception {
        this.template = template;
        this.findByEmail(email);
    }
    
    public void setName(String name) {this.name = name;}
    
    public void setLastname(String lastname) {this.lastname = lastname;}
    
    public void setEmil(String email) {this.email = email;}
    
    public void setPassword(String password) {
        byte[] encodedBytes = Base64.encodeBase64(password.getBytes());
        this.password = new String(encodedBytes);
    }
    
    public String getName() {return this.name;}
    
    public String getLastname() {return this.lastname;}
    
    public String getEmail() {return this.email;}
    
    public String getPassword() {return this.password;}
    
    public Map<String,Object> save() {
        return template.queryForMap(CypherQuery.CREATE_USER, id.toString(), name, lastname, email,password);
    }
    
    public Map<String,Object> findByEmail(String email) throws Exception {
        Map<String,Object> user_info = template.queryForMap(CypherQuery.GET_USER, email);
        this.email = email;
        if(user_info.containsKey("name"))
            this.name =(String) user_info.get("name");
        
        if(user_info.containsKey("lastname"))
            this.name =(String) user_info.get("lastname");
        
        this.id = (UUID) user_info.get("id");
        
        return user_info;
    }
    
    public void delete() throws Exception {
        //String iduser = id.toString();
        template.update(CypherQuery.DELETE_USER_BY_ID, id);
    }
}
