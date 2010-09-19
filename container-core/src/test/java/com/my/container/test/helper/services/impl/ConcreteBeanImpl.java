package com.my.container.test.helper.services.impl;

import com.my.container.test.helper.services.ConcreteBean;

/**
 * @author kevinpollet
 */
public class ConcreteBeanImpl extends AbstractBean implements ConcreteBean {

    @Override
    public String getOverriddenProtectedText() {
        return "Override" + super.getOverriddenProtectedText();
    }

    @Override
    public String getOverriddenPublicText() {
        return "Override" + super.getOverriddenPublicText();
    }

    @Override
    public String getNotOverriddenText() {
        return "NotOverriddenText";
    }

}
