package com.my.container.test.injection.services.impl.fields;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.impl.LowerEcho;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class FieldQualifierServiceD implements ServiceD {

    @Inject
    @LowerEcho
    private ServiceC serviceC;

    @Override
    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    @Override
    public int add(final int a, final int b) {
        return a+b;
    }
}
