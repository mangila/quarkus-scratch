package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String hello() {
        return "Hello!";
    }

    public String greet() {
        return "Greetings!";
    }

}
