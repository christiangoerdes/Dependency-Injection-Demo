package org.example.app.service;

import org.example.app.repo.RepoImpl1;

@org.example.framework.annotations.Service
public class ServiceImpl1 implements Service{

    RepoImpl1 repoImpl1;

    public ServiceImpl1(RepoImpl1 repoImpl1) {
        this.repoImpl1 = repoImpl1;
    }

    @Override
    public void doSomething() {
        repoImpl1.doSomething();
    }
}
