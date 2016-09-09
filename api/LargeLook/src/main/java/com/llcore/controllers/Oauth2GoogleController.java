/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llcore.controllers;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.UUID;
import com.llcore.libs.ResponseHandler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.llcore.externals.oauth2.CustomServerReceiver;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.llcore.entities.Email;
import java.util.ArrayList;
import com.llcore.utils.LlProperty;

//************************************************************
//************************************************************
//****************** IMPORTANT *******************************
//************************************************************
// ENABLE GMAIL API
// https://console.developers.google.com/apis/api/gmail/overview?project=<PROJECT_ID>

/**
 *
 * @author alejandro
 * Ejemplo interesante: https://developers.google.com/gmail/api/quickstart/java
 * https://developers.google.com/gmail/api/v1/reference/users/messages/list
 */
@RestController
public class Oauth2GoogleController {
    
    //own server
    private AuthorizationCodeFlow flow;
    private final String REDIRECT_URI; // = "http://ec2-52-39-224-145.us-west-2.compute.amazonaws.com:8080/LargeLook/oauth2/Callback"; //http://japp.com.es/oauth2callback";
    private static Oauth2 oauth2;
    private static Credential credential;
    private static HttpTransport httpTransport;
    private static FileDataStoreFactory dataStoreFactory;
    private static GoogleClientSecrets clientSecrets;
    AuthorizationCodeInstalledApp aca;
    private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY);
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    private static GoogleAuthorizationCodeFlow authorize() throws Exception {
        // load client secrets
          System.out.println("Dentro de Credentials: cargando credenciales");
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
            new InputStreamReader(Oauth2GoogleController.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
            || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
          System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
              + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
          System.exit(1);
        }
        System.out.println("Dentro de Credentials: configurando");
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(
            dataStoreFactory).build();
        // authorize
        System.out.println("Dentro de Credentials: Instanciando AuthorizationCodeInstalledApp");
        //AuthorizationCodeInstalledApp aca_ = new AuthorizationCodeInstalledApp(flow, new CustomServerReceiver());
        //System.out.println("autorizando usuario");
        //codigo fuente de AuthorizationCodeInstalledApp: https://github.com/cyberaleks/XClouds/blob/master/src/main/java/com/xclouds/auth/AuthorizationCodeInstalledApp.java
        return flow;
    }
    
    /**
     * 
     * @throws Exception 
     */
    public Oauth2GoogleController() throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        flow = authorize();
        LlProperty prop = new LlProperty();
        REDIRECT_URI = prop.getProperty("google_redirect_uri");
    }
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/oauth2/user", method = RequestMethod.GET)
    public ModelAndView getBasicNode() throws Exception {
        //credential = flow.loadCredential("user");
        /*if (credential != null
          && (credential.getRefreshToken() != null || (credential.getExpiresInSeconds()==null || credential.getExpiresInSeconds() > 60))) {
            ResponseHandler.ok("ok");
        }*/
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
        String auth_user_url = authorizationUrl.build();
        
        return new ModelAndView("redirect:" + auth_user_url);
    }
    
    /**
     * 
     * @param code
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/oauth2/Callback", method = RequestMethod.GET)
    public ResponseEntity<?> handleCode(
    @RequestParam(value = "code", required = true) String code) throws Exception {
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        credential = flow.createAndStoreCredential(response, "user");
        return ResponseHandler.ok("Ok");
    }
    
    /**
     * 
     * @param from
     * @param to
     * @param key_words
     * @param after
     * @param before
     * @param n
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/email/get_emails", method = RequestMethod.GET)
    public ResponseEntity<?> getEmailsBySender(
    @RequestParam(value = "from", required = false) String from,
    @RequestParam(value = "to", required = false) String to,
    @RequestParam(value = "key_words", required = false) String key_words,
    @RequestParam(value = "after", required = false) String after,
    @RequestParam(value = "before", required = false) String before,
    @RequestParam(value = "n", required = true) int n) throws Exception {
        //default query
        String query = "in:inbox";
        
        //filter by from
        if(from != null) {
            String[] senders_list = from.split(",");
            for(int i = 0; i < senders_list.length; i++)
                senders_list[i] = "from:"+senders_list[i];
            
            String senders_join = String.join(" ", senders_list);
            query += " {"+senders_join+"}";
        }
        
        //filter by to
        if(to != null) {
            String[] to_list = to.split(",");
            for(int i = 0; i < to_list.length; i++)
                to_list[i] = "to:"+to_list[i];
            
            String to_join = String.join(" ", to_list);
            query += " {"+to_join+"} ";
        }
        
        //filter after
        if(after != null)
            query += " after:" + after;
        
        //filter before
        if(before != null)
            query += " before:" + before;
        
        //key words
        if(key_words != null)
            query += " " + key_words.replace(",", " ");
        
        
        
        System.out.println("Query: "+query);
        
        Gmail gmail_service = new Gmail.Builder(httpTransport,JSON_FACTORY,credential).setApplicationName("japp").build();
        String user = "me"; 
        ListMessagesResponse listMessagesResponse = gmail_service.users().messages().list(user).setQ(query).execute();
        List<Message> messages = listMessagesResponse.getMessages();
        List<Email> emails = new ArrayList();
        
        int count = 1;
        for(Message message : messages) {
            Message this_message = gmail_service.users().messages().get(user, message.getId()).execute();
            Email email = new Email(this_message);
            emails.add(email);
            System.out.println(" message: "+this_message.getSnippet());
            if(count++ >= n)
                break;
        }
        
        return ResponseHandler.ok(emails);
    }
}
