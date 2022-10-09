/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.model.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElemCopierTest {

    @Test
    public void copierTest() {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        IntVar var2 = model.intVar(0, 10);
        IntVar var3 = model.intVar(0, 10);
        IntVar var4 = model.intVar(0, 10);
        model.constraint(var1.gt(var2).and(var2.gt(var3).and(var3.gt(var4))));
        Model copy = model.copy();
        assertEquals(model, copy);
    }
}
