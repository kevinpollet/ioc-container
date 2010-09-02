package com.my.container.test.callbacks.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * Implementation of the Basic service.
 * 
 * @author kevinpollet
 */
public class ParentImpl implements Parent {

    private String reference;

    private String leafReference;

    @Inject
    private Leaf leaf; 

    private int nbCallPostConstruct;
    private int nbCallPreDestroy;

    public ParentImpl() {
        this.nbCallPostConstruct = 0;
        this.nbCallPreDestroy = 0;
    }

    @PostConstruct
    private void init() {
        this.nbCallPostConstruct++;
        this.reference = "Parent";
        this.leafReference = this.leaf.getReference();
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public String getLeafReference() {
        return this.leafReference;
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
