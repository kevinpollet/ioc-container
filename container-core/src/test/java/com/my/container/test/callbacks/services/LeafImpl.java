package com.my.container.test.callbacks.services;

import javax.annotation.PostConstruct;

/**
 * @author kevinpollet
 */
public class LeafImpl implements Leaf {

    private String reference;

    @PostConstruct
    private void init() {
        this.reference = "Leaf";        
    }

    @Override
    public String getReference() {
        return this.reference;
    }

}
