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
public class ProjectController extends Neo4jDataSource {
    @Autowired
    JdbcTemplate template;
    
    //TODO: crete model for this controller
    //TODO: use oauth2 for user creation
    //TODO: create exception library
    
    /**
     * Create a simple node project
     * @param name
     * @param user_id
     * @return 
     */
    @RequestMapping(value = "/project/create", method = RequestMethod.POST)
    public ResponseEntity<?> createProject(   
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "user_id", required = true) String user_id) {
        Map<String,Object> result;
        UUID randomID = UUID.randomUUID();
        UUID relationship_randomID = UUID.randomUUID();
        result = template.queryForMap(CypherQuery.CREATE_PROJECT, randomID.toString(), name, user_id,relationship_randomID.toString());
        return ResponseHandler.ok(result);
    }
            
    /**
     * Delete an project
     * @return 
     */
    //TODO: deberia borra todos los nodos.
    @RequestMapping(value = "/project/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteProject() {
        return null;
    }
    
}
