/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMethod;
import com.llcore.libs.ResponseHandler;
import com.llcore.models.Node;
import com.llcore.models.Relationship;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author alejandro
 */
@RestController
public class NodeController extends Neo4jDataSource {
    
    /**
     * Create a new node
     * @param name
     * @param description
     * @param father_node_id
     * @param child_node_id
     * @return 
     */
    @RequestMapping(value = "/node/create", method = RequestMethod.POST)
    public ResponseEntity<?> createBasicNode(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "father_node_id", required = false) UUID father_node_id,
            @RequestParam(value = "child_node_id", required = false) UUID child_node_id) throws Exception {
        
        Map<String,Object> result;
        Node node = new Node(template);
        node.setName(name);
        node.setDescription(description);
        result = node.save();
        
        if(father_node_id != null) {
            Relationship relationship = new Relationship(template);
            relationship.setSourceId(father_node_id);
            relationship.setTargetId(node.getId());
            result = relationship.save();
            return ResponseHandler.ok(result);
        }
        
        if(child_node_id != null) {
            Relationship relationship = new Relationship(template);
            relationship.setSourceId(node.getId());
            relationship.setTargetId(child_node_id);
            result = relationship.save();
            return ResponseHandler.ok(result);
        }
        
        return ResponseHandler.ok(result);
    }
    
    /**
     * Delete a node
     * @param id
     * @return 
     */
    @RequestMapping(value = "/node/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteBasicNode(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        
        Node node = new Node(template,id);
        node.delete();
        return ResponseHandler.ok("done");
    }
    
    /**
     * Get a node
     * @param id
     * @return 
     */
    @RequestMapping(value = "/node/get", method = RequestMethod.GET)
    public ResponseEntity<?> getBasicNode(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        Node node = new Node(template,id);
        return ResponseHandler.ok(node.getAllInfo());
    }
    
    
}
