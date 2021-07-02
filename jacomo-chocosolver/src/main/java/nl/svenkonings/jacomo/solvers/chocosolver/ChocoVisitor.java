/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.arithmetic.NaArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor which builds a ChocoSolver model from the visited elements.
 */
@SuppressWarnings({"ConstantConditions", "SuspiciousNameCombination"})
public class ChocoVisitor implements Visitor<ChocoType> {
    private final @NotNull Model model;

    private final @NotNull Map<String, BoolVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    private final @NotNull Map<Elem, Constraint> constraintMap;
    private final @NotNull Map<Elem, ReExpression> reExpressionMap;
    private final @NotNull Map<Elem, ArExpression> arExpressionMap;

    /**
     * Create a new ChocoSolver visitor.
     */
    public ChocoVisitor() {
        model = new Model();
        boolVars = new HashMap<>();
        intVars = new HashMap<>();
        constraintMap = new HashMap<>();
        reExpressionMap = new HashMap<>();
        arExpressionMap = new HashMap<>();
    }

    /**
     * Returns the ChocoSolver model.
     *
     * @return the ChocoSolver model
     */
    public @NotNull Model getModel() {
        return model;
    }

    /**
     * Returns the mapping of boolean variable names to ChocoSolver variables.
     *
     * @return the mapping of boolean variable names to ChocoSolver variables
     */
    public @NotNull Map<String, BoolVar> getBoolVars() {
        return boolVars;
    }

    /**
     * Returns the mapping of integer variable names to ChocoSolver variables.
     *
     * @return the mapping of integer variable names to ChocoSolver variables
     */
    public @NotNull Map<String, IntVar> getIntVars() {
        return intVars;
    }

    private void addBoolVar(String name, BoolVar var) {
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

    private Constraint constraint(Elem elem) {
        if (constraintMap.containsKey(elem)) {
            return constraintMap.get(elem);
        }
        ChocoType result = visit(elem);
        Constraint constraint;
        if (result.isConstraint()) {
            constraint = result.getConstraint();
        } else if (result.isReExpression()) {
            ReExpression expr = result.getReExpression();
            try {
                constraint = expr.decompose();
            } catch (UnsupportedOperationException e) {
                constraint = expr.extension();
            }
        } else {
            throw new UnexpectedTypeException(elem);
        }
        constraintMap.put(elem, constraint);
        return constraint;
    }

    private ReExpression reExpression(Elem elem) {
        if (reExpressionMap.containsKey(elem)) {
            return reExpressionMap.get(elem);
        }
        ChocoType result = visit(elem);
        ReExpression expr;
        if (result.isReExpression()) {
            expr = result.getReExpression();
        } else if (result.isConstraint()) {
            expr = result.getConstraint().reify();
        } else {
            throw new UnexpectedTypeException(elem);
        }
        reExpressionMap.put(elem, expr);
        return expr;
    }

    private ArExpression arExpression(Elem elem) {
        if (arExpressionMap.containsKey(elem)) {
            return arExpressionMap.get(elem);
        }
        ChocoType result = visit(elem);
        ArExpression expr;
        if (result.isArExpression()) {
            expr = result.getArExpression();
        } else {
            throw new UnexpectedTypeException(elem);
        }
        arExpressionMap.put(elem, expr);
        return expr;
    }

    @Override
    public ChocoType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        Constraint constraint = constraint(boolExprConstraint.getExpr());
        constraint.post();
        return ChocoType.none();
    }

    @Override
    public ChocoType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        BoolVar var = model.boolVar(constantBoolExpr.getValue());
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitNotExpr(NotExpr notExpr) {
        ReExpression expr = reExpression(notExpr.getExpr());
        return ChocoType.reExpression(expr.not());
    }

    @Override
    public ChocoType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        BoolVar[] vars = collectAll(biBoolExpr).toArray(new BoolVar[0]);
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return ChocoType.constraint(model.and(vars));
            case OrExpr:
                return ChocoType.constraint(model.or(vars));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    // Collects all children of chained binary boolean expressions with the same type
    private List<BoolVar> collectAll(BiBoolExpr expr) {
        List<BoolVar> vars = new ArrayList<>();
        collectAll(expr, vars);
        return vars;
    }

    private void collectAll(BiBoolExpr expr, List<BoolVar> vars) {
        collectAll(expr, expr.getLeft(), vars);
        collectAll(expr, expr.getRight(), vars);
    }

    private void collectAll(BiBoolExpr expr, BoolExpr child, List<BoolVar> vars) {
        if (expr.getType() == child.getType()) {
            collectAll((BiBoolExpr) child, vars);
        } else {
            vars.add(reExpression(child).boolVar());
        }
    }

    @Override
    public ChocoType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        ArExpression left = arExpression(reBoolExpr.getLeft());
        ArExpression right = arExpression(reBoolExpr.getRight());
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return ChocoType.reExpression(left.eq(right));
            case NeExpr:
                return ChocoType.reExpression(left.ne(right));
            case GtExpr:
                return ChocoType.reExpression(left.gt(right));
            case GeExpr:
                return ChocoType.reExpression(left.ge(right));
            case LtExpr:
                return ChocoType.reExpression(left.lt(right));
            case LeExpr:
                return ChocoType.reExpression(left.le(right));
            default:
                throw new UnexpectedTypeException(reBoolExpr);
        }
    }

    @Override
    public ChocoType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        IntVar var = model.intVar(constantIntExpr.getValue());
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitBiIntExpr(BiIntExpr biIntExpr) {
        switch (biIntExpr.getType()) {
            case SubExpr:
            case DivExpr:
                return nonAssociativeBiIntExpr(biIntExpr);
            case AddExpr:
            case MulExpr:
            case MinExpr:
            case MaxExpr:
                return associativeBiIntExpr(biIntExpr);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private ChocoType nonAssociativeBiIntExpr(BiIntExpr biIntExpr) {
        ArExpression left = arExpression(biIntExpr.getLeft());
        ArExpression right = arExpression(biIntExpr.getRight());
        switch (biIntExpr.getType()) {
            case SubExpr:
                return ChocoType.arExpression(left.sub(right));
            case DivExpr:
                return ChocoType.arExpression(left.div(right));
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private ChocoType associativeBiIntExpr(BiIntExpr biIntExpr) {
        ArExpression[] children = collectAll(biIntExpr).toArray(new ArExpression[0]);
        ArExpression.Operator operator;
        switch (biIntExpr.getType()) {
            case AddExpr:
                operator = ArExpression.Operator.ADD;
                break;
            case MulExpr:
                operator = ArExpression.Operator.MUL;
                break;
            case MinExpr:
                operator = ArExpression.Operator.MIN;
                break;
            case MaxExpr:
                operator = ArExpression.Operator.MAX;
                break;
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
        return ChocoType.arExpression(new NaArExpression(operator, children));
    }

    // Collects all children of chained binary integer expressions with the same type
    private List<ArExpression> collectAll(BiIntExpr expr) {
        List<ArExpression> results = new ArrayList<>();
        collectAll(expr, results);
        return results;
    }

    private void collectAll(BiIntExpr expr, List<ArExpression> results) {
        collectAll(expr, expr.getLeft(), results);
        collectAll(expr, expr.getRight(), results);
    }

    private void collectAll(BiIntExpr expr, IntExpr child, List<ArExpression> results) {
        if (expr.getType() == child.getType()) {
            collectAll((BiIntExpr) child, results);
        } else {
            results.add(arExpression(child));
        }
    }

    @Override
    public ChocoType visitBoolVar(nl.svenkonings.jacomo.elem.variables.bool.BoolVar boolVar) {
        String name = boolVar.getName();
        BoolVar var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            if (boolVar.hasValue()) {
                var = model.boolVar(name, boolVar.getValue());
            } else {
                var = model.boolVar(name);
            }
            addBoolVar(name, var);
        }
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        BoolVar var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            var = reExpression(expressionBoolVar.getExpression()).boolVar();
            addBoolVar(name, var);
        }
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitIntVar(nl.svenkonings.jacomo.elem.variables.integer.IntVar intVar) {
        String name = intVar.getName();
        IntVar var;
        if (intVars.containsKey(name)) {
            var = intVars.get(name);
        } else {
            if (intVar.hasValue()) {
                var = model.intVar(name, intVar.getValue());
            } else {
                int lb = intVar.hasLowerBound() ? intVar.getLowerBound() : IntVar.MIN_INT_BOUND;
                int ub = intVar.hasUpperBound() ? intVar.getUpperBound() : IntVar.MAX_INT_BOUND;
                var = model.intVar(name, lb, ub);
            }
            addIntVar(name, var);
        }
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        IntVar var;
        if (intVars.containsKey(name)) {
            var = intVars.get(name);
        } else {
            var = arExpression(expressionIntVar.getExpression()).intVar();
            addIntVar(name, var);
        }
        return ChocoType.arExpression(var);
    }
}
