package com.my.container.test.callbacks.services.impl;

import com.my.container.test.callbacks.services.Leaf;

import javax.annotation.PostConstruct;

/**
 * @author kevinpollet
 */
public class LeafImpl implements Leaf {

    private String name;

    @PostConstruct
    private void init() {
        this.name = "Leaf";
    }

    @Override
    public String getName() {
        return this.name;
    }

}
