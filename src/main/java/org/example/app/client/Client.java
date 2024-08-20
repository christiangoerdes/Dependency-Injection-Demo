package org.example.app.client;

import org.example.app.service.ServiceImpl1;

public class Client {

    private final ServiceImpl1 service;

    // Dependency Injection via Constructor
    public Client(ServiceImpl1 service) {
        this.service = service;
    }

    public void doSomething() {
        service.doSomething();
    }

}
