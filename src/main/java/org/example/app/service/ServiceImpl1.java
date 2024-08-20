package org.example.app.service;

@org.example.framework.annotations.Service
public class ServiceImpl1 implements Service{

    @Override
    public void doSomething() {
        System.out.println("Do something 1");
    }
}
