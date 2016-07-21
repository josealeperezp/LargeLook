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
    
    public static String CREATE_NODE_WITH_RELATIONSHIP = 
            "match (node1:Basic {id : {4}}) " +
            "create (node2:Basic {id : {1}, name : {2}, description : {3}}) " +
            "create (node1)-[rel:RELATED_TO {id : {5}}]->(node2) " +
            "return * ";
    
    public static String DELETE_NODE_QUERY =
            "MATCH (node:Basic { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(node) " +
            "OPTIONAL MATCH (node)-[r2]->() " +
            "delete r1,r2,node";
    
    public static String GET_NODE_QUERY =
            "MATCH (node:Basic { id : {1} }) RETURN node.id as id, node.name as name, node.description as description";
    
    //CRUD relationships
    public static String CREATE_RELATIONSHIP =
            "match (node1:Basic {id : {1}}) " +
            "match (node2:Basic {id : {2}}) " +
            "create (node1)-[rel:RELATED_TO {id: {3}}]->(node2) " +
            "return *";
    
    public static String DELETE_RELATIONSHIP =
            "match (node1:Basic {id : {1}})-[rel:RELATED_TO]->(node2:Basic {id : {2}}) " +
            "delete x";
    
    public static String DELETE_RELATIONSHIP_BY_ID =
            "match (node1:Basic {id : {1}})-[rel:RELATED_TO {id : {2}}]->(node2:Basic {id : {3}}) " +
            "delete x";
    
    //CRUD user
    //TODO: delete user must be more intelligent
    public static String CREATE_USER =
            "CREATE (user:User {"
            + " id: {1}, "
            + " name : {2}, "
            + " lastname : {3}, "
            + " email : {4}, "
            + " password : {5} "
            + "}) return user.id as id, user.name as name, user.lastname as lastname, user.email as email";
    
    public static String DELETE_USR_BY_ID =
            "MATCH (user:User { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(user) " +
            "OPTIONAL MATCH (user)-[r2]->() " +
            "delete r1,r2,user";
    
    public static String DELETE_USR_BY_EMAIL =
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
}
