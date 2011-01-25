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
package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;

import javax.inject.Inject;

/**
 * Implementation of the Service A.
 *
 * @author kevinpollet
 */
public class ConstructorServiceAImpl implements ServiceA {

    private ServiceB serviceB;

    @Inject
    public ConstructorServiceAImpl(final ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String echo(final String string) {
        return string;
    }

    public String sayHelloTo(final String name) {
        return "Hello " + name;
    }
}
