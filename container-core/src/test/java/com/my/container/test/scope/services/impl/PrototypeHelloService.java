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
package com.my.container.test.scope.services.impl;

import com.my.container.test.scope.services.HelloService;

/**
 * The prototype scoped service
 * implementation.
 *
 * @author kevinpollet
 */
public class PrototypeHelloService implements HelloService {

    public String sayHello() {
        return "Hello";
    }

}
