/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.util.ElemUtil;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Visitor which builds a OR-Tools CP-SAT model from the visited elements.
 */
@SuppressWarnings("ConstantConditions")
public class OrToolsVisitor implements Visitor<OrToolsType> {

    private final @NotNull CpModel model;

    private final @NotNull Map<String, Literal> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    private final @NotNull Map<Elem, Constraint> constraintMap;
    private final @NotNull Map<Elem, Literal> boolVarMap;
    private final @NotNull Map<Elem, IntVar> intVarMap;

    private int genNameCounter;

    /**
     * Create a new OR-Tools visitor.
     */
    public OrToolsVisitor() {
        Loader.loadNativeLibraries();
        model = new CpModel();
        boolVars = new LinkedHashMap<>();
        intVars = new LinkedHashMap<>();
        constraintMap = new HashMap<>();
        boolVarMap = new HashMap<>();
        intVarMap = new HashMap<>();
        genNameCounter = 0;
    }

    /**
     * Returns the OR-Tools CP-SAT model.
     *
     * @return the OR-Tools CP-SAT model
     */
    public @NotNull CpModel getModel() {
        return model;
    }

    /**
     * Returns the mapping of boolean variable names to OR-Tools CP-SAT variables.
     *
     * @return the mapping of boolean variable names to OR-Tools CP-SAT variables
     */
    public @NotNull Map<String, Literal> getBoolVars() {
        return boolVars;
    }

    /**
     * Returns the mapping of integer variable names to OR-Tools CP-SAT variables.
     *
     * @return the mapping of integer variable names to OR-Tools CP-SAT variables
     */
    public @NotNull Map<String, IntVar> getIntVars() {
        return intVars;
    }

    private void addBoolVar(String name, Literal var) {
        if (boolVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        } else if (intVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, intVars.get(name), var);
        }
        boolVars.put(name, var);
    }

    private void addIntVar(String name, IntVar var) {
        if (intVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, intVars.get(name), var);
        } else if (boolVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        }
        intVars.put(name, var);
    }

    private String genName() {
        return "_cpsat" + genNameCounter++;
    }

    private Literal genBoolVar() {
        return model.newBoolVar(genName());
    }

    private IntVar genIntVar() {
        return model.newIntVar(Integer.MIN_VALUE, Integer.MAX_VALUE, genName());
    }

    private Constraint constraint(Elem elem) {
        if (constraintMap.containsKey(elem)) {
            return constraintMap.get(elem);
        }
        OrToolsType result = visit(elem);
        Constraint constraint;
        if (result.isConstraint()) {
            constraint = result.getConstraint();
        } else if (result.isBoolVar()) {
            constraint = model.addEquality(result.getBoolVar(), 1L);
        } else {
            throw new UnexpectedTypeException(elem);
        }
        constraintMap.put(elem, constraint);
        return constraint;
    }

    private Literal boolVar(Elem elem) {
        if (boolVarMap.containsKey(elem)) {
            return boolVarMap.get(elem);
        }
        OrToolsType result = visit(elem);
        Literal boolVar;
        if (result.isBoolVar()) {
            boolVar = result.getBoolVar();
        } else if (result.isConstraint()) {
            Literal var = genBoolVar();
            result.getConstraint().onlyEnforceIf(var);
            result.getInverseConstraint().onlyEnforceIf(var.not());
            boolVar = var;
        } else {
            throw new UnexpectedTypeException(elem);
        }
        boolVarMap.put(elem, boolVar);
        return boolVar;
    }

    private IntVar intVar(Elem elem) {
        if (intVarMap.containsKey(elem)) {
            return intVarMap.get(elem);
        }
        OrToolsType result = visit(elem);
        IntVar intVar;
        if (result.isIntVar()) {
            intVar = result.getIntVar();
        } else {
            throw new UnexpectedTypeException(elem);
        }
        intVarMap.put(elem, intVar);
        return intVar;
    }

    @Override
    public OrToolsType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        // Constraint are enforced by default
        constraint(boolExprConstraint.getExpr());
        return OrToolsType.none();
    }

    @Override
    public OrToolsType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        int value = constantBoolExpr.getValue() ? 1 : 0;
        return OrToolsType.intVar(model.newConstant(value));
    }

    @Override
    public OrToolsType visitNotExpr(NotExpr notExpr) {
        Literal var = boolVar(notExpr.getExpr()).not();
        return OrToolsType.boolVar(var);
    }

    @Override
    public OrToolsType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        Literal[] vars = ElemUtil.collectAll(biBoolExpr).stream()
                .map(this::boolVar)
                .toArray(Literal[]::new);
        switch (biBoolExpr.getType()) {
            case "AndExpr":
                return OrToolsType.constraint(model.addBoolAnd(vars),
                        () -> model.addBoolOr(Arrays.stream(vars).map(Literal::not).toArray(Literal[]::new)));
            case "OrExpr":
                return OrToolsType.constraint(model.addBoolOr(vars),
                        () -> model.addBoolAnd(Arrays.stream(vars).map(Literal::not).toArray(Literal[]::new)));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    public OrToolsType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        IntVar left = intVar(reBoolExpr.getLeft());
        IntVar right = intVar(reBoolExpr.getRight());
        switch (reBoolExpr.getType()) {
            case "EqExpr":
                return OrToolsType.constraint(model.addEquality(left, right),
                        () -> model.addDifferent(left, right));
            case "NeExpr":
                return OrToolsType.constraint(model.addDifferent(left, right),
                        () -> model.addEquality(left, right));
            case "GtExpr":
                return OrToolsType.constraint(model.addGreaterThan(left, right),
                        () -> model.addLessOrEqual(left, right));
            case "GeExpr":
                return OrToolsType.constraint(model.addGreaterOrEqual(left, right),
                        () -> model.addLessThan(left, right));
            case "LtExpr":
                return OrToolsType.constraint(model.addLessThan(left, right),
                        () -> model.addGreaterOrEqual(left, right));
            case "LeExpr":
                return OrToolsType.constraint(model.addLessOrEqual(left, right),
                        () -> model.addGreaterThan(left, right));
            default:
                throw new UnexpectedTypeException(reBoolExpr);
        }
    }

    @Override
    public OrToolsType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return OrToolsType.intVar(model.newConstant(constantIntExpr.getValue()));
    }

    @Override
    public OrToolsType visitBiIntExpr(BiIntExpr biIntExpr) {
        switch (biIntExpr.getType()) {
            case "SubExpr":
            case "MulExpr": // Although multiplication is associative, OR-Tools does not yet support flattening them
            case "DivExpr":
                return nonAssociativeBiIntExpr(biIntExpr);
            case "AddExpr":
            case "MinExpr":
            case "MaxExpr":
                return associativeBiIntExpr(biIntExpr);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private OrToolsType nonAssociativeBiIntExpr(BiIntExpr biIntExpr) {
        IntVar left = intVar(biIntExpr.getLeft());
        IntVar right = intVar(biIntExpr.getRight());
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case "SubExpr":
                // OPTIMIZATION: Combine multiple subtraction scalar expressions
                model.addEquality(var, LinearExpr.weightedSum(new IntVar[]{left, right}, new long[]{1L, -1L}));
                return OrToolsType.intVar(var);
            case "MulExpr":
                model.addMultiplicationEquality(var, new IntVar[]{left, right});
                return OrToolsType.intVar(var);
            case "DivExpr":
                // OR-Tools does not support negative integer division
                if (left.getDomain().min() < 0) {
                    IntVar oldLeftVar = left;
                    left = model.newIntVar(0, Integer.MAX_VALUE, genName());
                    model.addEquality(oldLeftVar, left);
                }
                if (right.getDomain().min() < 1) {
                    IntVar oldRightVar = right;
                    right = model.newIntVar(1, Integer.MAX_VALUE, genName());
                    model.addEquality(oldRightVar, right);
                }
                model.addDivisionEquality(var, left, right);
                return OrToolsType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private OrToolsType associativeBiIntExpr(BiIntExpr biIntExpr) {
        IntVar[] vars = ElemUtil.collectAll(biIntExpr).stream()
                .map(this::intVar)
                .toArray(IntVar[]::new);
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case "AddExpr":
                model.addEquality(var, LinearExpr.sum(vars));
                return OrToolsType.intVar(var);
            case "MinExpr":
                model.addMinEquality(var, vars);
                return OrToolsType.intVar(var);
            case "MaxExpr":
                model.addMaxEquality(var, vars);
                return OrToolsType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    public OrToolsType visitBoolVar(BoolVar boolVar) {
        String name = boolVar.getName();
        Literal var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            if (boolVar.hasValue()) {
                var = boolVar.getValue() ? model.trueLiteral() : model.falseLiteral();
            } else {
                var = model.newBoolVar(name);
            }
            addBoolVar(name, var);
        }
        return OrToolsType.boolVar(var);
    }

    @Override
    public OrToolsType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        Literal var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            var = boolVar(expressionBoolVar.getExpression());
            addBoolVar(name, var);
        }
        return OrToolsType.boolVar(var);
    }

    @Override
    public OrToolsType visitIntVar(nl.svenkonings.jacomo.elem.variables.integer.IntVar intVar) {
        String name = intVar.getName();
        IntVar var;
        if (intVars.containsKey(name)) {
            var = intVars.get(name);
        } else {
            if (intVar.hasValue()) {
                var = model.newConstant(intVar.getValue());
            } else {
                int lb = intVar.hasLowerBound() ? intVar.getLowerBound() : Integer.MIN_VALUE;
                int ub = intVar.hasUpperBound() ? intVar.getUpperBound() : Integer.MAX_VALUE;
                var = model.newIntVar(lb, ub, name);
            }
            addIntVar(name, var);
        }
        return OrToolsType.intVar(var);
    }

    @Override
    public OrToolsType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        IntVar var;
        if (intVars.containsKey(name)) {
            var = intVars.get(name);
        } else {
            var = intVar(expressionIntVar.getExpression());
            addIntVar(name, var);
        }
        return OrToolsType.intVar(var);
    }
}
