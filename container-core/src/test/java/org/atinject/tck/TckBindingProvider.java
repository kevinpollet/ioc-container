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
package org.atinject.tck;

import com.my.container.binding.provider.BindingProvider;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.FuelTank;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.SpareTire;

/**
 * @author Kevin Pollet
 */
public class TckBindingProvider extends BindingProvider {

    @Override
    public void configureBindings() {

        bind(Car.class).to(Convertible.class);
        bind(Seat.class).to(DriversSeat.class).qualifiedBy(Drivers.class);
        bind(Seat.class).to(Seat.class);
        bind(Tire.class).to(Tire.class);
        bind(Engine.class).to(V8Engine.class);
        bind(Tire.class).to(SpareTire.class).named("spare");
        bindClass(Cupholder.class);
        bindClass(SpareTire.class);
        bindClass(FuelTank.class);

    }

}
