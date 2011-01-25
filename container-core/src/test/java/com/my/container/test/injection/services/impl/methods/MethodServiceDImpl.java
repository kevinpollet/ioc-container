/*
 * Copyright 2011 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class MethodServiceDImpl extends MethodOverrideService implements ServiceD {

    private ServiceC serviceC;

    private ServiceE serviceE;

    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    public int add(int a, int b) {
        return this.serviceE.add(a, b);
    }

    @Inject
    @Override
    public void setServiceC(final ServiceC service) {
        this.nbCallSetServiceC++;
        this.serviceC = service;
    }


    @Inject
    public void setServiceE(final ServiceE service) {
        this.serviceE = service;
    }

}
