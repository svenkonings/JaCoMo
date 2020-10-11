package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.variables.integer.ExpressionIntVar;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor which builds a ChocoSolver model from the visited elements.
 */
@SuppressWarnings({"ConstantConditions", "SuspiciousNameCombination"})
public class ChocoVisitor extends Visitor<ChocoType> {
    private final @NotNull Model model;
    private final @NotNull Map<String, BoolVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    /**
     * Create a new ChocoSolver visitor.
     */
    public ChocoVisitor() {
        super();
        model = new Model();
        boolVars = new LinkedHashMap<>();
        intVars = new LinkedHashMap<>();
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
        ChocoType result = visit(elem);
        if (result.isConstraint()) {
            return result.getConstraint();
        } else if (result.isReExpression()) {
            ReExpression expr = result.getReExpression();
            try {
                return expr.decompose();
            } catch (UnsupportedOperationException e) {
                return expr.extension();
            }
        } else {
            throw new UnexpectedTypeException(elem);
        }
    }

    private ReExpression reExpression(Elem elem) {
        ChocoType result = visit(elem);
        if (result.isReExpression()) {
            return result.getReExpression();
        } else if (result.isConstraint()) {
            return result.getConstraint().reify();
        } else {
            throw new UnexpectedTypeException(elem);
        }
    }

    private ArExpression arExpression(Elem elem) {
        ChocoType result = visit(elem);
        if (result.isArExpression()) {
            return result.getArExpression();
        } else {
            throw new UnexpectedTypeException(elem);
        }
    }

    @Override
    protected ChocoType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        Constraint constraint = constraint(boolExprConstraint.getExpr());
        constraint.post();
        return ChocoType.none();
    }

    @Override
    protected ChocoType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        BoolVar boolVar = model.boolVar(constantBoolExpr.getValue());
        return ChocoType.reExpression(boolVar);
    }

    @Override
    protected ChocoType visitNotExpr(NotExpr notExpr) {
        ReExpression expr = reExpression(notExpr.getExpr());
        return ChocoType.reExpression(expr.not());
    }

    @Override
    protected ChocoType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
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
    protected ChocoType visitReBoolExpr(ReBoolExpr reBoolExpr) {
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
    protected ChocoType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        IntVar var = model.intVar(constantIntExpr.getValue());
        return ChocoType.arExpression(var);
    }

    @Override
    protected ChocoType visitBiIntExpr(BiIntExpr biIntExpr) {
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
    protected ChocoType visitBoolVar(nl.svenkonings.jacomo.variables.bool.BoolVar boolVar) {
        String name = boolVar.getName();
        BoolVar var;
        if (boolVar.hasValue()) {
            var = model.boolVar(name, boolVar.getValue());
        } else {
            var = model.boolVar(name);
        }
        addBoolVar(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    protected ChocoType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        BoolVar var = reExpression(expressionBoolVar.getExpression()).boolVar();
        addBoolVar(name, var);
        return ChocoType.reExpression(var);
    }

    @Override
    protected ChocoType visitIntVar(nl.svenkonings.jacomo.variables.integer.IntVar intVar) {
        String name = intVar.getName();
        IntVar var;
        if (intVar.hasValue()) {
            var = model.intVar(name, intVar.getValue());
        } else {
            int lb = intVar.hasLowerBound() ? intVar.getLowerBound() : IntVar.MIN_INT_BOUND;
            int ub = intVar.hasUpperBound() ? intVar.getUpperBound() : IntVar.MAX_INT_BOUND;
            var = model.intVar(name, lb, ub);
        }
        addIntVar(name, var);
        return ChocoType.arExpression(var);
    }

    @Override
    protected ChocoType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        IntVar var = arExpression(expressionIntVar.getExpression()).intVar();
        addIntVar(name, var);
        return ChocoType.arExpression(var);
    }
}
