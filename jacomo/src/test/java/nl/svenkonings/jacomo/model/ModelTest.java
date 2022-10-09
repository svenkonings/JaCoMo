/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.constraints.Constraint;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.ReservedNameException;
import nl.svenkonings.jacomo.util.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void varTest() {
        Model model = new Model();

        assertFalse(model.hasVars());
        assertFalse(model.containsVar("x"));
        assertFalse(model.containsVars(ListUtil.of("x")));
        assertNull(model.getVar("x"));
        assertTrue(model.getVars().isEmpty());
        assertTrue(model.getVarNames().isEmpty());
        assertEquals(0L, model.varStream().count());

        IntVar x = IntVar.variable("x");
        assertNull(model.addVar(x));
        assertTrue(model.hasVars());
        assertTrue(model.containsVar("x"));
        assertTrue(model.containsVars(ListUtil.of("x")));
        assertFalse(model.containsVars(ListUtil.of("x", "y")));
        assertEquals(x, model.getVar("x"));
        assertEquals(ListUtil.of(x), model.getVars());
        assertEquals(Collections.singleton("x"), model.getVarNames());
        assertEquals(1L, model.varStream().count());

        BoolVar x2 = BoolVar.constant("x", true);
        assertEquals(x, model.addVar(x2));
        assertTrue(model.hasVars());
        assertTrue(model.containsVar("x"));
        assertTrue(model.containsVars(ListUtil.of("x")));
        assertFalse(model.containsVars(ListUtil.of("x", "y")));
        assertEquals(x2, model.getVar("x"));
        assertEquals(ListUtil.of(x2), model.getVars());
        assertEquals(Collections.singleton("x"), model.getVarNames());
        assertEquals(1L, model.varStream().count());

        IntVar _x = IntVar.bounds("_x", 0, 2000);
        assertThrowsExactly(ReservedNameException.class, () -> model.addVar(BoolVar.variable("_x")));
        assertThrowsExactly(ReservedNameException.class, () -> model.addVar(_x));
        assertNull(model.addVarUnchecked(_x));
        assertTrue(model.containsVars(ListUtil.of("x", "_x")));
        assertEquals(_x, model.getVar("_x"));
        assertEquals(ListUtil.of(x2, _x), model.getVars());
        assertEquals(new HashSet<>(Arrays.asList("x", "_x")), model.getVarNames());
        assertEquals(2L, model.varStream().count());

        assertEquals(_x, model.removeVar("_x"));
        assertTrue(model.hasVars());
        assertTrue(model.containsVar("x"));
        assertTrue(model.containsVars(ListUtil.of("x")));
        assertFalse(model.containsVars(ListUtil.of("x", "y")));
        assertEquals(x2, model.getVar("x"));
        assertEquals(ListUtil.of(x2), model.getVars());
        assertEquals(Collections.singleton("x"), model.getVarNames());
        assertEquals(1L, model.varStream().count());
    }

    @Test
    public void constraintTest() {
        Model model = new Model();

        Constraint c1 = model.constraint(BoolExpr.constant(true));
        assertTrue(model.hasConstraints());
        assertTrue(model.containsConstraint(c1));
        assertTrue(model.containsConstraints(ListUtil.of(c1)));
        assertFalse(model.containsConstraints(ListUtil.of(c1, new BoolExprConstraint(BoolExpr.constant(false)))));
        assertEquals(ListUtil.of(c1), model.getConstraints());
        assertEquals(1L, model.constraintStream().count());

        // Add duplicate constraint
        model.constraint(BoolExpr.constant(true));
        assertTrue(model.hasConstraints());
        assertTrue(model.containsConstraint(c1));
        assertTrue(model.containsConstraints(ListUtil.of(c1)));
        assertFalse(model.containsConstraints(ListUtil.of(c1, new BoolExprConstraint(BoolExpr.constant(false)))));
        assertEquals(ListUtil.of(c1), model.getConstraints());
        assertEquals(1L, model.constraintStream().count());

        Constraint c2 = model.constraint(BoolExpr.constant(false));
        assertTrue(model.hasConstraints());
        assertTrue(model.containsConstraint(c2));
        assertTrue(model.containsConstraints(ListUtil.of(c1)));
        assertTrue(model.containsConstraints(ListUtil.of(c1, c2)));
        assertEquals(ListUtil.of(c1, c2), model.getConstraints());
        assertEquals(2L, model.constraintStream().count());

        assertTrue(model.removeConstraint(c2));
        assertTrue(model.hasConstraints());
        assertTrue(model.containsConstraint(c1));
        assertTrue(model.containsConstraints(ListUtil.of(c1)));
        assertFalse(model.containsConstraints(ListUtil.of(c1, new BoolExprConstraint(BoolExpr.constant(false)))));
        assertEquals(ListUtil.of(c1), model.getConstraints());
        assertEquals(1L, model.constraintStream().count());

        assertTrue(model.removeConstraints(ListUtil.of(c1)));
        assertFalse(model.hasConstraints());
        assertFalse(model.containsConstraint(c1));
        assertFalse(model.containsConstraints(ListUtil.of(c1)));
        assertEquals(ListUtil.of(), model.getConstraints());
        assertEquals(0L, model.constraintStream().count());
    }
}
