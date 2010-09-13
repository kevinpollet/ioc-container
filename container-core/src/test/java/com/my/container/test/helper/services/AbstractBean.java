package com.my.container.test.helper.services;

/**
 * @author kevinpollet
 */
public abstract class AbstractBean {

    protected String getProtectedText() {
        return "ProtectedText";
    }

    public String getText() {
        return "Text";
    }

}
