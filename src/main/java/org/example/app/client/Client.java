package org.example.app.client;

import org.example.app.service.Service;

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
