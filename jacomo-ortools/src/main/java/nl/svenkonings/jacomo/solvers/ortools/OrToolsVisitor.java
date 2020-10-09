package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.expressions.BiExpr;
import nl.svenkonings.jacomo.expressions.Expr;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.ReBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.BiIntExpr;
import nl.svenkonings.jacomo.variables.bool.BoolVar;
import nl.svenkonings.jacomo.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Visitor which builds a OR-Tools CP-SAT model from the visited elements.
 */
@SuppressWarnings("ConstantConditions")
public class OrToolsVisitor extends Visitor<OrToolsType> {

    private final @NotNull CpModel model;
    private final @NotNull Map<String, IntVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    private int genNameCounter;
    private final @NotNull List<IntVar> inverseVars;

    /**
     * Create a new OR-Tools visitor.
     */
    public OrToolsVisitor() {
        super();
        OrToolsLoader.loadLibrary();
        model = new CpModel();
        boolVars = new LinkedHashMap<>();
        intVars = new LinkedHashMap<>();
        genNameCounter = 0;
        inverseVars = new ArrayList<>();
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
    public @NotNull Map<String, IntVar> getBoolVars() {
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

    private void addBoolVar(String name, IntVar var) {
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

    private IntVar genBoolVar() {
        return model.newBoolVar(genName());
    }

    private IntVar genIntVar() {
        return model.newIntVar(Integer.MIN_VALUE, Integer.MAX_VALUE, genName());
    }

    private boolean inverseExists(int index) {
        return index < inverseVars.size() && inverseVars.get(index) != null;
    }

    private IntVar getInverse(IntVar boolVar) {
        if (inverseExists(boolVar.getIndex())) {
            return inverseVars.get(boolVar.getIndex());
        } else {
            IntVar inverseVar = genBoolVar();
            model.addDifferent(boolVar, inverseVar);
            setInverse(boolVar.getIndex(), inverseVar);
            setInverse(inverseVar.getIndex(), boolVar);
            return inverseVar;
        }
    }

    private void setInverse(int index, IntVar boolVar) {
        while (index >= inverseVars.size()) {
            inverseVars.add(null);
        }
        inverseVars.set(index, boolVar);
    }

    private IntVar constraintToVar(OrToolsType result) {
        if (!result.isConstraint()) {
            throw new UnexpectedTypeException("Expected constraint value, received: %s", result);
        }
        IntVar var = genBoolVar();
        result.getConstraint().onlyEnforceIf(var);
        IntVar inverseVar = getInverse(var);
        result.getInverseConstraint().onlyEnforceIf(inverseVar);
        return var;
    }

    // Collects all children of chained binary expressions with the same type
    private List<IntVar> collectAll(BiExpr expr) {
        List<IntVar> vars = new ArrayList<>();
        collectAll(expr, vars);
        return vars;
    }

    private void collectAll(BiExpr expr, List<IntVar> vars) {
        collectAll(expr, expr.getLeft(), vars);
        collectAll(expr, expr.getRight(), vars);
    }

    private void collectAll(BiExpr expr, Expr child, List<IntVar> vars) {
        if (expr.getType() == child.getType()) {
            collectAll((BiExpr) child, vars);
        } else {
            OrToolsType result = visit(child);
            if (!(result.isConstraint() || result.isIntVar())) {
                throw new UnexpectedTypeException(child);
            }
            IntVar var = result.isIntVar() ? result.getIntVar() : constraintToVar(result);
            vars.add(var);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected OrToolsType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        OrToolsType result = visit(boolExprConstraint.getExpr());
        if (result.isConstraint()) {
            // Constraint are enforced by default, do nothing
        } else if (result.isIntVar()) {
            // Assume the variable is a boolean, should be true (1)
            model.addEquality(result.getIntVar(), 1);
        } else {
            throw new UnexpectedTypeException(boolExprConstraint.getExpr());
        }
        return OrToolsType.none();
    }

    @Override
    protected OrToolsType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        int value = constantBoolExpr.getValue() ? 1 : 0;
        return OrToolsType.intVar(model.newConstant(value));
    }

    @Override
    protected OrToolsType visitNotExpr(NotExpr notExpr) {
        OrToolsType result = visit(notExpr.getExpr());
        IntVar var;
        if (result.isConstraint()) {
            var = getInverse(constraintToVar(result));
        } else if (result.isIntVar()) {
            var = getInverse(result.getIntVar());
        } else {
            throw new UnexpectedTypeException(notExpr.getExpr());
        }
        return OrToolsType.intVar(var);
    }

    @Override
    protected OrToolsType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        IntVar[] vars = collectAll(biBoolExpr).toArray(new IntVar[0]);
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return OrToolsType.constraint(model.addBoolAnd(vars),
                        () -> model.addBoolOr(Arrays.stream(vars).map(this::getInverse).toArray(IntVar[]::new)));
            case OrExpr:
                return OrToolsType.constraint(model.addBoolOr(vars),
                        () -> model.addBoolAnd(Arrays.stream(vars).map(this::getInverse).toArray(IntVar[]::new)));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    protected OrToolsType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        OrToolsType left = visit(reBoolExpr.getLeft());
        OrToolsType right = visit(reBoolExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getRight());
        }
        IntVar leftVar = left.getIntVar();
        IntVar rightVar = right.getIntVar();
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return OrToolsType.constraint(model.addEquality(leftVar, rightVar),
                        () -> model.addDifferent(leftVar, rightVar));
            case NeExpr:
                return OrToolsType.constraint(model.addDifferent(leftVar, rightVar),
                        () -> model.addEquality(leftVar, rightVar));
            case GtExpr:
                return OrToolsType.constraint(model.addGreaterThan(leftVar, rightVar),
                        () -> model.addLessOrEqual(leftVar, rightVar));
            case GeExpr:
                return OrToolsType.constraint(model.addGreaterOrEqual(leftVar, rightVar),
                        () -> model.addLessThan(leftVar, rightVar));
            case LtExpr:
                return OrToolsType.constraint(model.addLessThan(leftVar, rightVar),
                        () -> model.addGreaterOrEqual(leftVar, rightVar));
            case LeExpr:
                return OrToolsType.constraint(model.addLessOrEqual(leftVar, rightVar),
                        () -> model.addGreaterThan(leftVar, rightVar));
            default:
                throw new UnexpectedTypeException(reBoolExpr);
        }
    }

    @Override
    protected OrToolsType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return OrToolsType.intVar(model.newConstant(constantIntExpr.getValue()));
    }

    @Override
    protected OrToolsType visitBiIntExpr(BiIntExpr biIntExpr) {
        switch (biIntExpr.getType()) {
            case SubExpr:
            case MulExpr: // Although multiplication is associative, OR-Tools does not yet support flattening them
            case DivExpr:
                return nonAssociativeBiIntExpr(biIntExpr);
            case AddExpr:
            case MinExpr:
            case MaxExpr:
                return associativeBiIntExpr(biIntExpr);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private OrToolsType nonAssociativeBiIntExpr(BiIntExpr biIntExpr) {
        OrToolsType left = visit(biIntExpr.getLeft());
        OrToolsType right = visit(biIntExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getRight());
        }
        IntVar var = genIntVar();
        IntVar leftVar = left.getIntVar();
        IntVar rightVar = right.getIntVar();
        switch (biIntExpr.getType()) {
            case SubExpr:
                // OPTIMIZATION: Combine multiple subtraction scalar expressions
                model.addEquality(var, LinearExpr.scalProd(new IntVar[]{leftVar, rightVar}, new int[]{1, -1}));
                return OrToolsType.intVar(var);
            case MulExpr:
                model.addProductEquality(var, new IntVar[]{leftVar, rightVar});
                return OrToolsType.intVar(var);
            case DivExpr:
                // OR-Tools does not support negative integer division
                if (leftVar.getDomain().min() < 0) {
                    IntVar oldLeftVar = leftVar;
                    leftVar = model.newIntVar(0, Integer.MAX_VALUE, genName());
                    model.addEquality(oldLeftVar, leftVar);
                }
                if (rightVar.getDomain().min() < 1) {
                    IntVar oldRightVar = rightVar;
                    rightVar = model.newIntVar(1, Integer.MAX_VALUE, genName());
                    model.addEquality(oldRightVar, rightVar);
                }
                model.addDivisionEquality(var, leftVar, rightVar);
                return OrToolsType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    private OrToolsType associativeBiIntExpr(BiIntExpr biIntExpr) {
        IntVar[] vars = collectAll(biIntExpr).toArray(new IntVar[0]);
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case AddExpr:
                model.addEquality(var, LinearExpr.sum(vars));
                return OrToolsType.intVar(var);
            case MinExpr:
                model.addMinEquality(var, vars);
                return OrToolsType.intVar(var);
            case MaxExpr:
                model.addMaxEquality(var, vars);
                return OrToolsType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    protected OrToolsType visitBoolVar(BoolVar boolVar) {
        String name = boolVar.getName();
        IntVar var;
        if (boolVar.hasValue()) {
            int value = boolVar.getValue() ? 1 : 0;
            var = model.newConstant(value);
        } else {
            var = model.newBoolVar(name);
        }
        addBoolVar(name, var);
        return OrToolsType.intVar(var);
    }

    @Override
    protected OrToolsType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        OrToolsType result = visit(expressionBoolVar.getExpression());
        IntVar var;
        if (result.isConstraint()) {
            var = constraintToVar(result);
        } else if (result.isIntVar()) {
            var = result.getIntVar();
        } else {
            throw new UnexpectedTypeException(expressionBoolVar.getExpression());
        }
        addBoolVar(name, var);
        return OrToolsType.intVar(var);
    }

    @Override
    protected OrToolsType visitIntVar(nl.svenkonings.jacomo.variables.integer.IntVar intVar) {
        String name = intVar.getName();
        IntVar var;
        if (intVar.hasValue()) {
            var = model.newConstant(intVar.getValue());
        } else {
            int lb = intVar.hasLowerBound() ? intVar.getLowerBound() : Integer.MIN_VALUE;
            int ub = intVar.hasUpperBound() ? intVar.getUpperBound() : Integer.MAX_VALUE;
            var = model.newIntVar(lb, ub, name);
        }
        addIntVar(name, var);
        return OrToolsType.intVar(var);
    }

    @Override
    protected OrToolsType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        OrToolsType result = visit(expressionIntVar.getExpression());
        if (!result.isIntVar()) {
            throw new UnexpectedTypeException(expressionIntVar.getExpression());
        }
        IntVar var = result.getIntVar();
        addIntVar(name, var);
        return OrToolsType.intVar(var);
    }
}
