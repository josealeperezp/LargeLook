/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.llcore.Neo4jDataSource;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.llcore.libs.ResponseHandler;
import org.springframework.http.ResponseEntity;
import com.llcore.models.Relationship;
import java.util.List;
import com.llcore.exceptions.InvalidParamsException;

/**
 *
 * @author alejandro
 */
@RestController
public class RelationshipController extends Neo4jDataSource {
       
    /**
     * Create a new relationship
     * @param start_node_id
     * @param end_node_id
     * @return 
     */
    @RequestMapping(value = "/relationship/create", method = RequestMethod.POST)
    public ResponseEntity<?> createRelationship(   
            @RequestParam(value = "start_node_id", required = true) UUID start_node_id, 
            @RequestParam(value = "end_node_id", required = true) UUID end_node_id,
            @RequestParam(value = "rel_label", required = false, defaultValue = "RELATED_TO") String rel_label) throws Exception {
       
        Relationship rel = new Relationship(template);
        rel.setSourceId(start_node_id);
        rel.setTargetId(end_node_id);
        return ResponseHandler.ok(rel.save(rel_label));
    }
    
    /**
     * Delete a relationship
     * @param start_node_id
     * @param end_node_id
     * @param relationship_id
     * @return 
     */
    @RequestMapping(value = "/relationship/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRelationship(
            @RequestParam(value = "start_node_id", required = false) UUID start_node_id, 
            @RequestParam(value = "end_node_id", required = false) UUID end_node_id,
            @RequestParam(value = "relationship_id", required = false) UUID relationship_id) throws Exception {
        
        Relationship rel;
        
        if(relationship_id != null)
            rel = new Relationship(template,relationship_id);
        else if(start_node_id != null && end_node_id != null)
            rel = new Relationship(template,start_node_id,end_node_id);
        else
            throw new InvalidParamsException("Bad arguments. You need to specified relationship_id or start_node_id and end_node_id");
        
        boolean rels = rel.delete();
        return ResponseHandler.ok(rels);
        
    }
    
    /**
     * Returns relationships between two nodes. Or returns the information about the give relationship ID
     * @param start_node_id
     * @param end_node_id
     * @param relationship_id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/relationship/get", method = RequestMethod.GET)
    public ResponseEntity<?> getRelationship(
            @RequestParam(value = "start_node_id", required = false) UUID start_node_id, 
            @RequestParam(value = "end_node_id", required = false) UUID end_node_id,
            @RequestParam(value = "relationship_id", required = false) UUID relationship_id) throws Exception {
        
        Relationship rel = new Relationship(template);

        if(relationship_id != null) {
            Map<String,Object> rels = rel.findOneById(relationship_id);
            return ResponseHandler.ok(rels);
        } else if(start_node_id != null && end_node_id != null){
            List<Map<String,Object>> rels = rel.findAllByNodes(start_node_id, end_node_id);
            return ResponseHandler.ok(rels);
        } else {
            throw new InvalidParamsException("Bad arguments. You need to specified relationship_id or start_node_id and end_node_id");
        }
    }
}
