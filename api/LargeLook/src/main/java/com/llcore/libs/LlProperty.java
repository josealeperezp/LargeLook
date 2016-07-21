/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author alejandro
 */
public class LlProperty {
    Properties properties;
    
    public LlProperty() throws Exception {
        this.properties = new Properties();
        String propFileName = "llapi.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        
        if(inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file doesn't exists");
        }
    }
    
    public String getProperty(String prop_key) {
        return properties.getProperty(prop_key);
    }
    
}
