package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.exceptions.UnexpectedTypeException;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.*;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.*;
import nl.svenkonings.jacomo.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.variables.bool.InstantiatableBoolVar;
import nl.svenkonings.jacomo.variables.integer.BoundedIntVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import nl.svenkonings.jacomo.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class ChocoVisitor implements Visitor<ChocoType> {
    private final @NotNull Model model;
    private final @NotNull Map<String, Variable> vars;

    public ChocoVisitor() {
        model = new Model();
        vars = new HashMap<>();
    }

    public @NotNull Model getModel() {
        return model;
    }

    public @NotNull Map<String, Variable> getVars() {
        return vars;
    }

    @Override
    public ChocoType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        ChocoType result = visit(boolExprConstraint.getExpr());
        if (result.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        Constraint constraint = result.isConstraint() ? result.getConstraint() : result.getReExpression().decompose();
        constraint.post();
        return ChocoType.none();
    }

    @Override
    public ChocoType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        BoolVar boolVar = model.boolVar(constantBoolExpr.getValue());
        return ChocoType.reExpression(boolVar);
    }

    @Override
    public ChocoType visitAndExpr(AndExpr andExpr) {
        ChocoType left = visit(andExpr.getLeft());
        ChocoType right = visit(andExpr.getRight());
        if (left.isArExpression() || right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        Constraint leftConstraint = left.isConstraint() ? left.getConstraint() : left.getReExpression().decompose();
        Constraint rightConstraint = right.isConstraint() ? right.getConstraint() : right.getReExpression().decompose();
        Constraint constraint = model.and(leftConstraint, rightConstraint);
        return ChocoType.constraint(constraint);
    }

    @Override
    public ChocoType visitOrExpr(OrExpr orExpr) {
        ChocoType left = visit(orExpr.getLeft());
        ChocoType right = visit(orExpr.getRight());
        if (left.isArExpression() || right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        Constraint leftConstraint = left.isConstraint() ? left.getConstraint() : left.getReExpression().decompose();
        Constraint rightConstraint = right.isConstraint() ? right.getConstraint() : right.getReExpression().decompose();
        Constraint constraint = model.or(leftConstraint, rightConstraint);
        return ChocoType.constraint(constraint);
    }

    @Override
    public ChocoType visitEqExpr(EqExpr eqExpr) {
        ChocoType left = visit(eqExpr.getLeft());
        ChocoType right = visit(eqExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().eq(right.getArExpression()));
    }

    @Override
    public ChocoType visitNeExpr(NeExpr neExpr) {
        ChocoType left = visit(neExpr.getLeft());
        ChocoType right = visit(neExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().ne(right.getArExpression()));
    }

    @Override
    public ChocoType visitGtExpr(GtExpr gtExpr) {
        ChocoType left = visit(gtExpr.getLeft());
        ChocoType right = visit(gtExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().gt(right.getArExpression()));
    }

    @Override
    public ChocoType visitGeExpr(GeExpr geExpr) {
        ChocoType left = visit(geExpr.getLeft());
        ChocoType right = visit(geExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().ge(right.getArExpression()));
    }

    @Override
    public ChocoType visitLtExpr(LtExpr ltExpr) {
        ChocoType left = visit(ltExpr.getLeft());
        ChocoType right = visit(ltExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().lt(right.getArExpression()));
    }

    @Override
    public ChocoType visitLeExpr(LeExpr leExpr) {
        ChocoType left = visit(leExpr.getLeft());
        ChocoType right = visit(leExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.reExpression(left.getArExpression().le(right.getArExpression()));
    }

    @Override
    public ChocoType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        IntVar var = model.intVar(constantIntExpr.getValue());
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitAddExpr(AddExpr addExpr) {
        ChocoType left = visit(addExpr.getLeft());
        ChocoType right = visit(addExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().add(right.getArExpression()));
    }

    @Override
    public ChocoType visitSubExpr(SubExpr subExpr) {
        ChocoType left = visit(subExpr.getLeft());
        ChocoType right = visit(subExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().sub(right.getArExpression()));
    }

    @Override
    public ChocoType visitMulExpr(MulExpr mulExpr) {
        ChocoType left = visit(mulExpr.getLeft());
        ChocoType right = visit(mulExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().mul(right.getArExpression()));
    }

    @Override
    public ChocoType visitDivExpr(DivExpr divExpr) {
        ChocoType left = visit(divExpr.getLeft());
        ChocoType right = visit(divExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().div(right.getArExpression()));
    }

    @Override
    public ChocoType visitMinExpr(MinExpr minExpr) {
        ChocoType left = visit(minExpr.getLeft());
        ChocoType right = visit(minExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().min(right.getArExpression()));
    }

    @Override
    public ChocoType visitMaxExpr(MaxExpr maxExpr) {
        ChocoType left = visit(maxExpr.getLeft());
        ChocoType right = visit(maxExpr.getRight());
        if (!left.isArExpression() || !right.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        return ChocoType.arExpression(left.getArExpression().max(right.getArExpression()));
    }

    @Override
    public ChocoType visitConstantBoolVar(ConstantBoolVar constantBoolVar) {
        String name = constantBoolVar.getName();
        BoolVar var = model.boolVar(name, constantBoolVar.getValue());
        vars.put(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        BoolVar var;
        ChocoType result = visit(expressionBoolVar.getExpression());
        if (result.isConstraint()) {
            Constraint constraint = result.getConstraint();
            var = constraint.reify();
        } else if (result.isReExpression()) {
            ReExpression reExpression = result.getReExpression();
            var = reExpression.boolVar();
        } else {
            throw new UnexpectedTypeException();
        }
        vars.put(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitInstantiatableBoolVar(InstantiatableBoolVar instantiatableBoolVar) {
        String name = instantiatableBoolVar.getName();
        BoolVar var;
        if (instantiatableBoolVar.hasValue()) {
            var = model.boolVar(name, instantiatableBoolVar.getValue());
        } else {
            var = model.boolVar(name);
        }
        vars.put(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitConstantIntVar(ConstantIntVar constantIntVar) {
        String name = constantIntVar.getName();
        IntVar var = model.intVar(name, constantIntVar.getValue());
        vars.put(name, var);
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        ChocoType result = visit(expressionIntVar.getExpression());
        if (!result.isArExpression()) {
            throw new UnexpectedTypeException();
        }
        IntVar var = result.getArExpression().intVar();
        vars.put(name, var);
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitBoundedIntVar(BoundedIntVar boundedIntVar) {
        String name = boundedIntVar.getName();
        IntVar var;
        if (boundedIntVar.hasValue()) {
            var = model.intVar(name, boundedIntVar.getValue());
        } else {
            int lb = boundedIntVar.hasLowerBound() ? boundedIntVar.getLowerBound() : IntVar.MIN_INT_BOUND;
            int ub = boundedIntVar.hasUpperBound() ? boundedIntVar.getUpperBound() : IntVar.MAX_INT_BOUND;
            var = model.intVar(name, lb, ub);
        }
        vars.put(name, var);
        return ChocoType.arExpression(var);
    }
}
