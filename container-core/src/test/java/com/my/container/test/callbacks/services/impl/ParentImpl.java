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
package com.my.container.test.callbacks.services.impl;

import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.Parent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ParentImpl implements Parent {

    private String name;

    private String leafName;

    @Inject
    private Leaf leaf;

    private int nbCallPostConstruct;
    private int nbCallPreDestroy;

    public ParentImpl() {
        this.nbCallPostConstruct = 0;
        this.nbCallPreDestroy = 0;
    }

    @PostConstruct
    private void init() {
        this.nbCallPostConstruct++;
        this.name = "Parent";
        this.leafName = this.leaf.getName();
    }

    public String getName() {
        return this.name;
    }

    public String getLeafName() {
        return this.leafName;
    }

    public int getNbCallPostConstruct() {
        return this.nbCallPostConstruct;
    }

    public int getNbCallPreDestroy() {
        return this.nbCallPreDestroy;
    }

    @PreDestroy
    private void destroy() {
        this.nbCallPreDestroy++;
    }

}
