package org.example;

import org.example.app.client.Client;
import org.example.app.service.Service;
import org.example.app.service.ServiceImpl1;
import org.example.app.service.ServiceImpl2;
import org.example.framework.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = new Injector();

        // Register the service implementation
        injector.register(Service.class, new ServiceImpl1());
        injector.register(Service.class, new ServiceImpl2());

        // Resolve dependencies and create a Client instance
        Client client = new Client(injector.resolve(Service.class));

        // Use the client
        client.doSomething();

    }
}