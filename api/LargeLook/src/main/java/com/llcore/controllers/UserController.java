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

/**
 *
 * @author alejandro
 */
@RestController
public class UserController extends Neo4jDataSource {
    @Autowired
    JdbcTemplate template;
    
    //TODO: crete model for this controller
    //TODO: use oauth2 for user creation
    //TODO: create exception library
    
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
            @RequestParam(value = "password", required = true) String password) {
        Map<String,Object> result;
        UUID randomID = UUID.randomUUID();
        byte[] encodedBytes = Base64.encodeBase64(password.getBytes());
        result = template.queryForMap(CypherQuery.CREATE_USER, randomID.toString(), name, lastname, email,new String(encodedBytes));
        return ResponseHandler.ok(result);
    }
            
    /**
     * Delete an user
     * @param id
     * @param email
     * @return 
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUser(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "email", required = false) String email
        ) {
        int result;
        if(id != null) {
            result = template.update(CypherQuery.DELETE_USR_BY_ID,id);
            return ResponseHandler.ok(result);
        } else if(email != null) { 
            result = template.update(CypherQuery.DELETE_USR_BY_EMAIL,email);
            return ResponseHandler.ok(result);
        } else {
            return ResponseHandler.internal_error("Id or email must be provided");
        }
    }
    
}
