/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hello;

import com.hello.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author intraffic
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue = "Shit") String name) {
        return new Greeting(
                counter.incrementAndGet(),
                String.format(template, name)
        );
    }
    
}
