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
public class InvalidProjectException extends Exception {
    String message;
    
    public InvalidProjectException(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
}
