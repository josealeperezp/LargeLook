/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.libs; 

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author alejandro
 */
public class CypherQuery {
    /**
     * TODO: improve cypher queries
     */
    //CRUD nodes
    public static String CREATE_NODE_QUERY =
            "CREATE (node:Basic { id: {1}, name : {2}, description : {3} }) return node.id as id, node.name as name";
    
    public static String CREATE_NODE_WITH_RELATIONSHIP(String rel_label) { 
            return "match (node1:Basic {id : {4}}) " +
            "create (node2:Basic {id : {1}, name : {2}, description : {3}}) " +
            "create (node1)-[rel:"+rel_label+" {id : {5}}]->(node2) " +
            "return * ";
    }
    
    public static String DELETE_NODE_QUERY =
            "MATCH (node:Basic { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(node) " +
            "OPTIONAL MATCH (node)-[r2]->() " +
            "delete r1,r2,node";
    
    public static String GET_NODE_QUERY =
            "MATCH (node:Basic { id : {1} }) RETURN node.id as id, node.name as name, node.description as description";
    
    //CRUD relationships
    public static String CREATE_RELATIONSHIP(String rel_label) {
            return "match (node1 {id : {1}}) " +
            "match (node2:Basic {id : {2}}) " +
            "create (node1)-[rel:"+rel_label.trim()+" {id: {3}}]->(node2) " +
            "return *";
    }
    
    public static String DELETE_RELATIONSHIP =
            "match (node1:Basic {id : {1}})-[rel]->(node2:Basic {id : {2}}) " +
            "delete rel";
    
    public static String DELETE_RELATIONSHIP_BY_ID =
            "match (node1:Basic)-[rel {id : {1}}]->(node2:Basic) " +
            "delete rel";
    
    public static String GET_RELATIONSHIP_BY_ID = 
            "match (node1:Basic)-[rel {id : {1}}]->(node2:Basic) return node1, node2, rel";
    
    public static String DELETE_RELATIONSHIP_BY_NODE =
            "match (node1:Basic {id : {1}})-[rel:RELATED_TO]->(node2:Basic {id : {2}}) " +
            "return node1, node2, rel";
    
    //CRUD user
    //TODO: delete user must be more intelligent
    public static String GET_USER =
            "match (user:User { email : {1} }) return user.email as email, user.name as name, user.id as id, user.lastname as lastname";
    
    public static String CREATE_USER =
            "CREATE (user:User {"
            + " id: {1}, "
            + " name : {2}, "
            + " lastname : {3}, "
            + " email : {4}, "
            + " password : {5} "
            + "}) return user.id as id, user.name as name, user.lastname as lastname, user.email as email";
    
    public static String DELETE_USER_BY_ID =
            "MATCH (user:User { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(user) " +
            "OPTIONAL MATCH (user)-[r2]->() " +
            "delete r1,r2,user";
    
    public static String DELETE_USER_BY_EMAIL =
            "MATCH (user:User { email : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(user) " +
            "OPTIONAL MATCH (user)-[r2]->() " +
            "delete r1,r2,user";
    
    //CRUD project
    public static String CREATE_PROJECT = 
            "match (user:User {id : {3}}) " +
            "create (project:Project {id : {1}, name : {2}}) " +
            "create (user)-[rel:HAS_PROJECT {id : {4}}]->(project) " +
            "return * ";
    
    public static String GET_PROJECT = 
            "match (project:Project { id : {1} }) return project.name as name, project.id as id";
    
    public static String DELETE_PROJECT =
            "MATCH (project:Project { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(project) " +
            "OPTIONAL MATCH (project)-[r2]->() " +
            "delete r1,r2,project";
    
    public static String GET_PROJECT_BY_USER = 
            "match (user:User {id : {1}})-[rel:HAS_PROJECT]->(project:Project) return project";
    
    //CRUD tree
    public static String CREATE_TREE = 
            "match (project:Project {id : {3}}) " +
            "create (root:Root {id : {1}, name : {2}}) " +
            "create (project)-[rel:HAS_TREE {id : {4}}]->(root) " +
            "return *";
    
    public static String GET_TREE = 
            "match (root:Root {id : {1}})-[rel*]->(child) return *";
    
    public static String DELETE_TREE = 
            "MATCH (a)<-[rels*]-(t {id : {1}})-[rels2*0..1]-() " +
            "FOREACH(r in rels2 | DELETE r) " +
            "FOREACH(r in rels | DELETE r) " +
            "DELETE t,a return count(*) as n";
    
    public static String DELETE_TREE_NODE = 
            "MATCH (node:Basic { id : {1} })<-[rel]-() " +
            "delete rel,node return count(*) as n";
}
