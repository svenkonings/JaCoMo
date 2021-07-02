/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.constraints.Constraint;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.UnBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.CheckException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import nl.svenkonings.jacomo.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Visitor which checks the validity of the model and builds and optimized variant.
 * <p>
 * The following validity checks are used:
 * <ul>
 *     <li>No conflicting variable names</li>
 *     <li>No known false constraints</li>
 * </ul>
 * <p>
 * The following optimizations are used:
 * <ul>
 *     <li>Replace resolved variables and expressions with constants</li>
 *     <li>Only include unresolved top-level variables and constraints</li>
 *     <li>Remove duplicate variables and constraints</li>
 * </ul>
 */
@SuppressWarnings({"ConstantConditions", "SwitchStatementWithTooFewBranches"})
public class Checker implements Visitor<Elem> {
    private final @NotNull Map<Elem, Elem> checkedElems;
    private final @NotNull Map<String, BoolVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    /**
     * Create a new Checker.
     */
    public Checker() {
        checkedElems = new HashMap<>();
        boolVars = new HashMap<>();
        intVars = new HashMap<>();
    }

    /**
     * Checks the specified model and returns and optimized model.
     *
     * @param model the specified model
     * @return the optimized model
     * @throws CheckException if one of the checks fails
     */
    public @NotNull Model check(Model model) throws CheckException {
        List<Elem> visitedVars = model.visitVars(this);
        List<Elem> visitedConstraints = model.visitConstraints(this);
        checkedElems.clear();
        boolVars.clear();
        intVars.clear();

        Set<Var> vars = new HashSet<>();
        for (Elem elem : visitedVars) {
            // Resolved vars have been replaced by expressions
            if (elem instanceof Var) {
                vars.add((Var) elem);
            }
        }

        Set<Constraint> constraints = new HashSet<>();
        for (Elem elem : visitedConstraints) {
            // Do not add resolved constraints
            if (elem instanceof BoolExprConstraint && ((BoolExprConstraint) elem).getExpr().hasValue()) {
                // Check if resolved constraint holds
                if (!(((BoolExprConstraint) elem).getExpr().getValue())) {
                    throw new CheckException("The following constraint is always false: %s", elem);
                }
            } else {
                constraints.add((Constraint) elem);
            }
        }

        Model result = new Model();
        result.addVarsUnchecked(vars);
        result.addConstraints(constraints);
        return result;
    }

    @Override
    public Elem visit(Elem elem) throws UnknownTypeException {
        if (checkedElems.containsKey(elem)) {
            return checkedElems.get(elem);
        } else {
            Elem checkedElem = Visitor.super.visit(elem);
            checkedElems.put(elem, checkedElem);
            return checkedElem;
        }
    }

    private IntExpr intConst(IntExpr intExpr) {
        return (IntExpr) visit(new ConstantIntExpr(intExpr.getValue()));
    }

    private BoolExpr boolConst(BoolExpr boolExpr) {
        return (BoolExpr) visit(new ConstantBoolExpr(boolExpr.getValue()));
    }

    @SuppressWarnings("DuplicatedCode")
    private void addBoolVar(BoolVar var) {
        String name = var.getName();
        if (boolVars.containsKey(name) && !boolVars.get(name).equals(var)) {
            throw new CheckException("Variable %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        } else if (intVars.containsKey(name)) {
            throw new CheckException("Variable %s already exists. Var1: %s, Var2: %s", name, intVars.get(name), var);
        }
        boolVars.put(name, var);
    }

    @SuppressWarnings("DuplicatedCode")
    private void addIntVar(IntVar var) {
        String name = var.getName();
        if (intVars.containsKey(name) && !intVars.get(name).equals(var)) {
            throw new CheckException("Variable %s already exists. Var1: %s, Var2: %s", name, intVars.get(name), var);
        } else if (boolVars.containsKey(name)) {
            throw new CheckException("Variable %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        }
        intVars.put(name, var);
    }

    @Override
    public Elem visitElem(Elem elem) {
        return elem;
    }

    @Override
    public Elem visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        return new BoolExprConstraint((BoolExpr) visit(boolExprConstraint.getExpr()));
    }

    @Override
    public Elem visitBoolExpr(BoolExpr boolExpr) {
        if (boolExpr.hasValue()) {
            return boolConst(boolExpr);
        } else {
            return boolExpr;
        }
    }

    @Override
    public Elem visitUnBoolExpr(UnBoolExpr unBoolExpr) {
        if (unBoolExpr.hasValue()) {
            return boolConst(unBoolExpr);
        }
        BoolExpr expr = (BoolExpr) visit(unBoolExpr.getExpr());
        switch (unBoolExpr.getType()) {
            case NotExpr:
                return expr.not();
            default:
                throw new UnexpectedTypeException(unBoolExpr);
        }
    }

    @Override
    public Elem visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        if (biBoolExpr.hasValue()) {
            return boolConst(biBoolExpr);
        }
        BoolExpr left = (BoolExpr) visit(biBoolExpr.getLeft());
        BoolExpr right = (BoolExpr) visit(biBoolExpr.getRight());
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return left.and(right);
            case OrExpr:
                return left.or(right);
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    public Elem visitReBoolExpr(ReBoolExpr reBoolExpr) {
        if (reBoolExpr.hasValue()) {
            return boolConst(reBoolExpr);
        }
        IntExpr left = (IntExpr) visit(reBoolExpr.getLeft());
        IntExpr right = (IntExpr) visit(reBoolExpr.getRight());
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return left.eq(right);
            case NeExpr:
                return left.ne(right);
            case GtExpr:
                return left.gt(right);
            case GeExpr:
                return left.ge(right);
            case LtExpr:
                return left.lt(right);
            case LeExpr:
                return left.le(right);
            default:
                throw new UnexpectedTypeException(reBoolExpr);
        }
    }

    @Override
    public Elem visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return constantBoolExpr;
    }

    @Override
    public Elem visitIntExpr(IntExpr intExpr) {
        if (intExpr.hasValue()) {
            return intConst(intExpr);
        } else {
            return intExpr;
        }
    }

    @Override
    public Elem visitBiIntExpr(BiIntExpr biIntExpr) {
        if (biIntExpr.hasValue()) {
            return intConst(biIntExpr);
        }
        IntExpr left = (IntExpr) visit(biIntExpr.getLeft());
        IntExpr right = (IntExpr) visit(biIntExpr.getRight());
        switch (biIntExpr.getType()) {
            case AddExpr:
                return left.add(right);
            case SubExpr:
                return left.sub(right);
            case MulExpr:
                return left.mul(right);
            case DivExpr:
                return left.div(right);
            case MinExpr:
                return left.min(right);
            case MaxExpr:
                return left.max(right);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    public Elem visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return constantIntExpr;
    }

    @Override
    public Elem visitBoolVar(BoolVar boolVar) {
        addBoolVar(boolVar);
        if (boolVar.hasValue()) {
            return boolConst(boolVar);
        } else {
            return boolVar;
        }
    }

    @Override
    public Elem visitIntVar(IntVar intVar) {
        addIntVar(intVar);
        if (intVar.hasValue()) {
            return intConst(intVar);
        } else {
            return intVar;
        }
    }
}
