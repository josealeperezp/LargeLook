/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import com.llcore.libs.ResponseHandler;
import com.llcore.libs.CypherQuery;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Base64;
import com.llcore.models.User;

/**
 *
 * @author alejandro
 */
@RestController
public class UserController extends Neo4jDataSource {
    //TODO: crete model for this controller
    //TODO: use oauth2 for user creation
    
    /**
     * Create a simple node user
     * @param name
     * @param lastname
     * @param email
     * @param password
     * @return 
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(   
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password) throws Exception {
        
        User user = new User(template);
        user.setEmil(email);
        user.setPassword(password);
        user.setName(name);
        user.setLastname(lastname);
        return ResponseHandler.ok(user.save());
    }
            
    /**
     * Delete an user
     * @param id
     * @param email
     * @return 
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUser(
            @RequestParam(value = "email", required = true) String email) throws Exception {
        
        User user = new User(template,email);
        user.delete();
        return ResponseHandler.ok(true);
        
    }
    
}
