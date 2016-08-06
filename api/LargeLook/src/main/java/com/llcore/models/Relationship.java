/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import com.llcore.libs.CypherQuery;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import com.llcore.exceptions.DefaultNeo4jException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alejandro
 */
public class Relationship {
    JdbcTemplate template;
    UUID id;
    UUID source_id;
    UUID target_id;
    List<Map<String,Object>> relationships = new ArrayList<Map<String,Object>>();
    
    boolean keep;
    
    /**
     * 
     * @param template 
     */
    public Relationship(JdbcTemplate template){
        this.template = template;
        this.keep = false;
    }
    
    public Relationship(JdbcTemplate template, UUID start_node_id, UUID end_node_id) throws Exception {
        this.template = template;
        this.keep = true;
        this.findAllByNodes(start_node_id, end_node_id);
    }
    
    public Relationship(JdbcTemplate template, UUID rel_id) throws Exception {
        this.template = template;
        this.keep = true;
        this.findOneById(rel_id);
    }
    
    /**
     * 
     * @return 
     */
    public UUID getId() {return id;}
    
    public void setId(UUID id) {this.id = id;}
    
    /**
     * 
     * @param source_id 
     */
    public void setSourceId(UUID source_id) {this.source_id = source_id;}
    
    /**
     * 
     * @return 
     */
    public UUID getSourceId(){return source_id;}
    
    /**
     * 
     * @param target_id 
     */
    public void setTargetId(UUID target_id) {this.target_id = target_id;}
    
    /**
     * 
     * @return 
     */
    public UUID getTargetId(){return target_id;}
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    public Map<String,Object> save(String rel_label) throws Exception {
        UUID randomID = UUID.randomUUID();
        if(source_id == null | target_id == null)
            throw new DefaultNeo4jException("source_id or target_id is null");
        
        return template.queryForMap(CypherQuery.CREATE_RELATIONSHIP(rel_label), source_id.toString(), target_id.toString(),randomID.toString());
    }
    
    public Map<String,Object> findOneById(UUID id) throws Exception {
        if(id != null) {
            Map<String,Object> result = template.queryForMap(CypherQuery.GET_RELATIONSHIP_BY_ID, id.toString());
            if(this.keep)
                this.relationships.add(result);
            return result;
        }  
        
        return null;
    }
    
    public List<Map<String,Object>> findAllByNodes(UUID start, UUID end) throws Exception {
        if(start != null && end != null) {
            List<Map<String,Object>> results = template.queryForList(CypherQuery.DELETE_RELATIONSHIP_BY_NODE, start.toString(), end.toString());
            if(this.keep)
                this.relationships = results;
            return results;
        }  
        
        return null;
    }
    
    public boolean delete() throws Exception {
        boolean deleted = false;
        
        for(Map<String,Object> rel : this.relationships) {
            Map<String,Object> rel_tag = (Map<String,Object>) rel.get("rel");
            template.update(CypherQuery.DELETE_RELATIONSHIP_BY_ID, rel_tag.get("id").toString());
            deleted = true;
        }
        return deleted;
    }
}
