package com.my.container.test.callbacks.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Implementation of the Basic service.
 * 
 * @author kevinpollet
 */
public class ServiceImpl implements Service {

    private int nbCallPostConstruct;
    private int nbCallPreDestroy;

    public ServiceImpl() {
        this.nbCallPostConstruct = 0;
        this.nbCallPreDestroy = 0;
    }

    @PostConstruct
    private void init() {
        this.nbCallPostConstruct++;
    }

    @Override
    public String sayHello() {
        return "Hello";
    }

    public int getNbCallPostConstruct() {
        return this.nbCallPostConstruct;
    }
    
    public int getNbCallPreDestroy() {
        return this.nbCallPreDestroy;
    }

    @PreDestroy
    private void destroy() {
        this.nbCallPreDestroy++;
    }

}
