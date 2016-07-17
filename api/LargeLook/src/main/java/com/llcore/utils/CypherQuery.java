/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.utils;

/**
 *
 * @author alejandro
 */
public class CypherQuery {
    //CRUD nodes
    public static String CREATE_NODE_QUERY =
            "CREATE (n:Basic { id: {1}, name : {2}, description : {3} }) return n.id as id, n.name as name";
    
    public static String CREATE_NODE_WITH_RELATIONSHIP = 
            "match (a:Basic {id : {4}}) " +
            "create (b:Basic {id : {1}, name : {2}, description : {3}}) " +
            "create (a)-[x:RELATED_TO {id : {5}}]->(b) " +
            "return * ";
    
    public static String DELETE_NODE_QUERY =
            "MATCH (n:Basic { id : {1} }) " +
            "OPTIONAL MATCH ()-[r1]->(n) " +
            "OPTIONAL MATCH (n)-[r2]->() " +
            "delete r1,r2,n";
    
    public static String GET_NODE_QUERY =
            "MATCH (n:Basic { id : {1} }) RETURN n.id as id, n.name as name, n.description as description";
    
    //CRUD relationships
    public static String CREATE_RELATIONSHIP =
            "match (a:Basic {id : {1}}) " +
            "match (b:Basic {id : {2}}) " +
            "create (a)-[x:RELATED_TO {id: {3}}]->(b) " +
            "return *";
    
    public static String DELETE_RELATIONSHIP =
            "match (a:Basic {id : {1}})-[x:RELATED_TO]->(b:Basic {id : {2}}) " +
            "delete x";
    
    public static String DELETE_RELATIONSHIP_BY_ID =
            "match (a:Basic {id : {1}})-[x:RELATED_TO {id : {2}}]->(b:Basic {id : {3}}) " +
            "delete x";
}
