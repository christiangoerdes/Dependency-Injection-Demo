package org.example;

import org.example.app.client.Client;
import org.example.app.service.Service;
import org.example.app.service.ServiceImpl1;
import org.example.framework.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = new Injector();

        injector.registerImplementation(Service.class, ServiceImpl1.class);

        // Resolve dependencies and create a Client instance
        Client client1 = injector.resolve(Client.class);
        Client client2 = injector.resolve(Client.class);

        // Use the client
        client1.doSomething();
        client2.doSomething();
    }
}