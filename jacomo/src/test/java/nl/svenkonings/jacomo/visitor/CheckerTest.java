/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.util.ListUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckerTest {

    @Test
    public void checkTest() {
        Model model = new Model();
        model.intVar("x", IntVar.variable("y").div(IntExpr.constant(6).div(IntExpr.constant(3))));
        model.constraint(BoolExpr.constant(true));
        Model checked = model.check();
        assertEquals(ListUtil.of(IntVar.expression("x", IntVar.variable("y").div(IntExpr.constant(2)))), checked.getVars());
        assertTrue(checked.getConstraints().isEmpty());
    }
}
