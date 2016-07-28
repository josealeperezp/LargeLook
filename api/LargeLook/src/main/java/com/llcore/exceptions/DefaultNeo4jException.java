/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.exceptions;

/**
 *
 * @author alejandro
 */
public class DefaultNeo4jException extends Exception {
    String message;
    
    public DefaultNeo4jException(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}
