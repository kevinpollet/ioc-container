package com.my.container.test.helper.servcices;

/**
 * @author kevinpollet
 */
public class ConcreteBeanImpl extends AbstractBean implements ConcreteBean {

    @Override
    public String getProtectedText() {
        return "Override" + super.getProtectedText();
    }

    @Override
    public String getText() {
        return super.getText() + "concreteText";
    }

    @Override
    public String getSecondText() {
        return "SecondText";
    }

}
