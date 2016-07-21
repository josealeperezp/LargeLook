/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.libs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author alejandro
 */
public class ResponseHandler {
    public ResponseHandler(){}
    
    public static ResponseEntity<?> ok(Object object) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(object,responseHeaders,HttpStatus.OK);
    }
    
    public static ResponseEntity<?> internal_error(Object object) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(object,responseHeaders,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
