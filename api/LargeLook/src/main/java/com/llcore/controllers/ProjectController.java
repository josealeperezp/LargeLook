/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import com.llcore.libs.ResponseHandler;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.llcore.models.Project;
/**
 *
 * @author alejandro
 */
@RestController
public class ProjectController extends Neo4jDataSource {
    
    /**
     * Create a simple node project
     * @param name
     * @param user_id
     * @return 
     */
    @RequestMapping(value = "/project/create", method = RequestMethod.POST)
    public ResponseEntity<?> createProject(   
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "user_id", required = true) UUID user_id) throws Exception {
        Project project = new Project(template);
        project.setName(name);
        project.setUserId(user_id);
        return ResponseHandler.ok(project.save());
    }
            
    /**
     * Delete an project
     * @param id
     * @return 
     */
    @RequestMapping(value = "/project/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProject(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        Project project = new Project(template,id,Project.PROJECT);
        project.delete();
        return ResponseHandler.ok(true);
    }
    
    /**
     * Get a project
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/project/get", method = RequestMethod.GET)
    public ResponseEntity<?> getProject(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        Project project = new Project(template,id,Project.PROJECT);
        return ResponseHandler.ok(project.getFullInfo());
    }
    
    /**
     * Get a list of project given an user id
     * @param user_id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/project/get_by_user", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectByUser(
            @RequestParam(value = "user_id", required = true) UUID user_id) throws Exception {
        Project project = new Project(template,user_id,Project.USER);
        return ResponseHandler.ok(project.getFullInfoByUser());
    }
    
    
}
