/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import com.llcore.libs.ResponseHandler;
import com.llcore.models.Node;
import com.llcore.models.Relationship;
import com.llcore.models.Tree;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alejandro
 */
@RestController
public class TreeController extends Neo4jDataSource {
    /**
     * Creates an empty tree
     * @param name
     * @param project_id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/tree/create", method = RequestMethod.POST)
    public ResponseEntity<?> createTree(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "project_id", required = true) UUID project_id) throws Exception {
        Tree tree = new Tree(template);
        tree.setName(name);
        tree.setProjectId(project_id);
        return ResponseHandler.ok(tree.save());
    }
    
    /**
     * Creates a tree node
     * @param name
     * @param description
     * @param father_node_id
     * @param rel_label
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/tree/node/create", method = RequestMethod.POST)
    public ResponseEntity<?> createNodeTree(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "father_node_id", required = true) UUID father_node_id,
            @RequestParam(value = "rel_label", required = false, defaultValue = "RELATED_TO") String rel_label) throws Exception {
        
        Map<String,Object> result;
        Node node = new Node(template);
        node.setName(name);
        node.setDescription(description);
        node.save();        
        
        Relationship relationship = new Relationship(template);
        relationship.setSourceId(father_node_id);
        relationship.setTargetId(node.getId());
        result = relationship.save(rel_label);
        return ResponseHandler.ok(result);
        
    }
    
    /**
     * Get a tree
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/tree/get", method = RequestMethod.GET)
    public ResponseEntity<?> getTree(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        
        Tree node = new Tree(template,id);
        return ResponseHandler.ok(node.getTree());
        
    }
    
    /**
     * Delete a tree (subtree)
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/tree/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTree(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        
        Tree tree = new Tree(template);
        int rows_affected = tree.delete(id);
        HashMap<String,Integer> result = new HashMap<String,Integer>();
        result.put("rows_affected",rows_affected);
        return ResponseHandler.ok(result);
        
    }
    
    /**
     * Delete a tree node
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/tree/node/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTreeNode(
            @RequestParam(value = "id", required = true) UUID id) throws Exception {
        
        Tree tree = new Tree(template);
        int rows_affected = tree.deleteNode(id);
        HashMap<String,Integer> result = new HashMap<String,Integer>();
        result.put("rows_affected",rows_affected);
        return ResponseHandler.ok(result);
        
    }
}
