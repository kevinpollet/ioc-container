package com.my.container.test.helper.services.impl;

/**
 * @author kevinpollet
 */
public abstract class AbstractBean {

    protected String getOverriddenProtectedText() {
        return "ProtectedText";
    }

    public String getOverriddenPublicText() {
        return "PublicText";
    }

}
