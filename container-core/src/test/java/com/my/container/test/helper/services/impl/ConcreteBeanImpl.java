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

    public String getNotOverriddenText() {
        return "NotOverriddenText";
    }

}
