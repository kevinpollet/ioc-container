package com.my.container.test.callbacks.services.impl;

import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.Parent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ParentImpl implements Parent {

    private String name;

    private String leafName;

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
        this.name = "Parent";
        this.leafName = this.leaf.getName();
    }

    public String getName() {
        return this.name;
    }

    public String getLeafName() {
        return this.leafName;
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
