package org.example.client;

import org.example.service.Service;

public class Client {

    private final Service service;

    // Dependency Injection via Constructor
    public Client(Service service) {
        this.service = service;
    }

    public void doSomething() {
        service.doSomething();
    }

}
