/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.models;

import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author alejandro
 */
public class Project {
    JdbcTemplate template;
    UUID id;
    
    public Project(JdbcTemplate template) {
        this.template = template;
    }
}
