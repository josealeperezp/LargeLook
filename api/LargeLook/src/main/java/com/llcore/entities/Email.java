/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.entities;

import com.google.api.services.gmail.model.*;
import java.util.List;


/**
 *
 * @author alejandro
 */
public class Email {
    private String subject;
    private String sender;
    private String snippet;
    
    public Email(Message message){
        snippet = message.getSnippet();
        List<MessagePartHeader> message_header = message.getPayload().getHeaders();
        for(MessagePartHeader mph : message_header) {
            if(mph.getName().equals("Subject")) {
                subject = mph.getValue();
            }
            
            if(mph.getName().equals("From")) {
                sender = mph.getValue();
            }
            
            if(subject != null && sender != null)
                break;
        }
    }
    
    public String getSubject() {return this.subject;}
    public String getSender() {return this.sender;}
    public String getSnippet() {return this.snippet;}
    
    
}
