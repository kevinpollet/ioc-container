/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author kevinpollet
 */
public class MethodNamedServiceA implements ServiceA {

    private ServiceC serviceC;

    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    public String sayHelloTo(final String name) {
        return "Hello " + name;
    }

    @Inject
    public void setServiceC(@Named("upperEchoService") final ServiceC serviceC) {
        this.serviceC = serviceC;
    }
}
