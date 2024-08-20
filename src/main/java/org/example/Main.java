package org.example;

import org.example.app.client.Client;
import org.example.framework.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = new Injector();

        // Resolve dependencies and create a Client instance
        Client client1 = injector.resolve(Client.class);
        Client client2 = injector.resolve(Client.class);

        // Use the client
        client1.doSomething();
        client2.doSomething();
    }
}