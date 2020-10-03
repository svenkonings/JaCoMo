package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.variables.bool.ExpressionBoolVar;
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

/**
 * Visitor which builds a ChocoSolver model from the visited elements
 */
@SuppressWarnings("ConstantConditions")
public class ChocoVisitor implements Visitor<ChocoType> {
    private final @NotNull Model model;
    private final @NotNull Map<String, Variable> vars;

    /**
     * Create a new ChocoSolver visitor.
     */
    public ChocoVisitor() {
        model = new Model();
        vars = new HashMap<>();
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
     * Returns the mapping of element variable names to ChocoSolver variables.
     *
     * @return the mapping of element variable names to ChocoSolver variables
     */
    public @NotNull Map<String, Variable> getVars() {
        return vars;
    }

    @Override
    public ChocoType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        ChocoType result = visit(boolExprConstraint.getExpr());
        if (result.isArExpression()) {
            throw new UnexpectedTypeException(boolExprConstraint.getExpr());
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
    public ChocoType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        ChocoType left = visit(biBoolExpr.getLeft());
        ChocoType right = visit(biBoolExpr.getRight());
        if (left.isArExpression()) {
            throw new UnexpectedTypeException(biBoolExpr.getLeft());
        } else if (right.isArExpression()) {
            throw new UnexpectedTypeException(biBoolExpr.getRight());
        }
        Constraint leftConstraint = left.isConstraint() ? left.getConstraint() : left.getReExpression().decompose();
        Constraint rightConstraint = right.isConstraint() ? right.getConstraint() : right.getReExpression().decompose();
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return ChocoType.constraint(model.and(leftConstraint, rightConstraint));
            case OrExpr:
                return ChocoType.constraint(model.or(leftConstraint, rightConstraint));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    public ChocoType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        ChocoType left = visit(reBoolExpr.getLeft());
        ChocoType right = visit(reBoolExpr.getRight());
        if (!left.isArExpression()) {
            throw new UnexpectedTypeException(reBoolExpr.getLeft());
        } else if (!right.isArExpression()) {
            throw new UnexpectedTypeException(reBoolExpr.getRight());
        }
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return ChocoType.reExpression(left.getArExpression().eq(right.getArExpression()));
            case NeExpr:
                return ChocoType.reExpression(left.getArExpression().ne(right.getArExpression()));
            case GtExpr:
                return ChocoType.reExpression(left.getArExpression().gt(right.getArExpression()));
            case GeExpr:
                return ChocoType.reExpression(left.getArExpression().ge(right.getArExpression()));
            case LtExpr:
                return ChocoType.reExpression(left.getArExpression().lt(right.getArExpression()));
            case LeExpr:
                return ChocoType.reExpression(left.getArExpression().le(right.getArExpression()));
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
        ChocoType left = visit(biIntExpr.getLeft());
        ChocoType right = visit(biIntExpr.getRight());
        if (!left.isArExpression()) {
            throw new UnexpectedTypeException(biIntExpr.getLeft());
        } else if (!right.isArExpression()) {
            throw new UnexpectedTypeException(biIntExpr.getRight());
        }
        switch (biIntExpr.getType()) {
            case AddExpr:
                return ChocoType.arExpression(left.getArExpression().add(right.getArExpression()));
            case SubExpr:
                return ChocoType.arExpression(left.getArExpression().sub(right.getArExpression()));
            case MulExpr:
                return ChocoType.arExpression(left.getArExpression().mul(right.getArExpression()));
            case DivExpr:
                return ChocoType.arExpression(left.getArExpression().div(right.getArExpression()));
            case MinExpr:
                return ChocoType.arExpression(left.getArExpression().min(right.getArExpression()));
            case MaxExpr:
                return ChocoType.arExpression(left.getArExpression().max(right.getArExpression()));
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    public ChocoType visitBoolVar(nl.svenkonings.jacomo.variables.bool.BoolVar boolVar) {
        String name = boolVar.getName();
        BoolVar var;
        if (boolVar.hasValue()) {
            var = model.boolVar(name, boolVar.getValue());
        } else {
            var = model.boolVar(name);
        }
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
            throw new UnexpectedTypeException(expressionBoolVar.getExpression());
        }
        vars.put(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    public ChocoType visitIntVar(nl.svenkonings.jacomo.variables.integer.IntVar intVar) {
        String name = intVar.getName();
        IntVar var;
        if (intVar.hasValue()) {
            var = model.intVar(name, intVar.getValue());
        } else {
            int lb = intVar.hasLowerBound() ? intVar.getLowerBound() : IntVar.MIN_INT_BOUND;
            int ub = intVar.hasUpperBound() ? intVar.getUpperBound() : IntVar.MAX_INT_BOUND;
            var = model.intVar(name, lb, ub);
        }
        vars.put(name, var);
        return ChocoType.arExpression(var);
    }

    @Override
    public ChocoType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        ChocoType result = visit(expressionIntVar.getExpression());
        if (!result.isArExpression()) {
            throw new UnexpectedTypeException(expressionIntVar.getExpression());
        }
        IntVar var = result.getArExpression().intVar();
        vars.put(name, var);
        return ChocoType.arExpression(var);
    }
}