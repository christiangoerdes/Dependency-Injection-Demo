package org.example;

import org.example.client.Client;
import org.example.injector.Injector;
import org.example.service.Service;
import org.example.service.ServiceImpl;

public class Main {
    public static void main(String[] args) {
        Injector injector = new Injector();

        // Register the service implementation
        injector.register(Service.class, new ServiceImpl());

        // Resolve dependencies and create a Client instance
        Client client = new Client(injector.resolve(Service.class));

        // Use the client
        client.doSomething();

    }
}