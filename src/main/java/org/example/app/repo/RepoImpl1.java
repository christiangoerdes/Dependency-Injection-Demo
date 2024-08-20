package org.example.app.repo;

import org.example.app.service.ServiceImpl1;

import static java.lang.Math.random;

public class RepoImpl1 implements Repo{

    double ranNum;

    public RepoImpl1() {
        ranNum = random();
    }

    @Override
    public void doSomething() {
        System.out.println("Do something Repo: " + ranNum);
    }
}
