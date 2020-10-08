package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Visitor which builds a OR-Tools CP-SAT model from the visited elements.
 */
@SuppressWarnings("ConstantConditions")
public class OrToolsVisitor extends Visitor<OrToolsType> {

    private final @NotNull CpModel model;
    private final @NotNull Map<String, IntVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;
    private int genNameCounter;

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

    static private class ConstraintVar {
        private final @NotNull IntVar var;
        private final @NotNull IntVar inverseVar;

        public ConstraintVar(@NotNull IntVar var, @NotNull IntVar inverseVar) {
            this.var = var;
            this.inverseVar = inverseVar;
        }

        public @NotNull IntVar getVar() {
            return var;
        }

        public @NotNull IntVar getInverseVar() {
            return inverseVar;
        }
    }

    private ConstraintVar constraintToVar(OrToolsType result) {
        if (!result.isConstraint()) {
            throw new UnexpectedTypeException("Expected constraint value, received: %s", result);
        }
        IntVar var = genBoolVar();
        result.getConstraint().onlyEnforceIf(var);
        IntVar inverseVar = getInverse(var);
        result.getInverseConstraint().onlyEnforceIf(inverseVar);
        return new ConstraintVar(var, inverseVar);
    }

    private IntVar getInverse(IntVar boolVar) {
        IntVar var = genBoolVar();
        model.addDifferent(boolVar, var);
        return var;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public OrToolsType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        OrToolsType result = visit(boolExprConstraint.getExpr());
        if (result.isConstraint()) {
            // Do nothing
        } else if (result.isIntVar()) {
            // Assume the variable is a boolean and should be true (1)
            model.addEquality(result.getIntVar(), 1);
        } else {
            throw new UnexpectedTypeException(boolExprConstraint.getExpr());
        }
        return OrToolsType.none();
    }

    @Override
    public OrToolsType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        int value = constantBoolExpr.getValue() ? 1 : 0;
        return OrToolsType.intVar(model.newConstant(value));
    }

    @Override
    public OrToolsType visitNotExpr(NotExpr notExpr) {
        OrToolsType result = visit(notExpr.getExpr());
        IntVar var;
        if (result.isConstraint()) {
            var = constraintToVar(result).getInverseVar();
        } else if (result.isIntVar()) {
            var = getInverse(result.getIntVar());
        } else {
            throw new UnexpectedTypeException(notExpr.getExpr());
        }
        return OrToolsType.intVar(var);
    }

    @Override
    public OrToolsType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        OrToolsType left = visit(biBoolExpr.getLeft());
        OrToolsType right = visit(biBoolExpr.getRight());
        IntVar leftVar;
        IntVar rightVar;
        if (left.isConstraint()) {
            leftVar = constraintToVar(left).getVar();
        } else if (left.isIntVar()) {
            leftVar = left.getIntVar();
        } else {
            throw new UnexpectedTypeException(biBoolExpr.getLeft());
        }
        if (right.isConstraint()) {
            rightVar = constraintToVar(right).getVar();
        } else if (right.isIntVar()) {
            rightVar = right.getIntVar();
        } else {
            throw new UnexpectedTypeException(biBoolExpr.getLeft());
        }
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return OrToolsType.constraint(model.addBoolAnd(new Literal[]{leftVar, rightVar}),
                        () -> model.addBoolOr(new Literal[]{getInverse(leftVar), getInverse(rightVar)}));
            case OrExpr:
                return OrToolsType.constraint(model.addBoolOr(new Literal[]{leftVar, rightVar}),
                        () -> model.addBoolAnd(new Literal[]{getInverse(leftVar), getInverse(rightVar)}));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    public OrToolsType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        OrToolsType left = visit(reBoolExpr.getLeft());
        OrToolsType right = visit(reBoolExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getRight());
        }
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return OrToolsType.constraint(model.addEquality(left.getIntVar(), right.getIntVar()),
                        () -> model.addDifferent(left.getIntVar(), right.getIntVar()));
            case NeExpr:
                return OrToolsType.constraint(model.addDifferent(left.getIntVar(), right.getIntVar()),
                        () -> model.addEquality(left.getIntVar(), right.getIntVar()));
            case GtExpr:
                return OrToolsType.constraint(model.addGreaterThan(left.getIntVar(), right.getIntVar()),
                        () -> model.addLessOrEqual(left.getIntVar(), right.getIntVar()));
            case GeExpr:
                return OrToolsType.constraint(model.addGreaterOrEqual(left.getIntVar(), right.getIntVar()),
                        () -> model.addLessThan(left.getIntVar(), right.getIntVar()));
            case LtExpr:
                return OrToolsType.constraint(model.addLessThan(left.getIntVar(), right.getIntVar()),
                        () -> model.addGreaterOrEqual(left.getIntVar(), right.getIntVar()));
            case LeExpr:
                return OrToolsType.constraint(model.addLessOrEqual(left.getIntVar(), right.getIntVar()),
                        () -> model.addGreaterThan(left.getIntVar(), right.getIntVar()));
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
        OrToolsType left = visit(biIntExpr.getLeft());
        OrToolsType right = visit(biIntExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getRight());
        }
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case AddExpr:
                model.addEquality(var, LinearExpr.sum(new IntVar[]{left.getIntVar(), right.getIntVar()}));
                return OrToolsType.intVar(var);
            case SubExpr:
                model.addEquality(var, LinearExpr.scalProd(new IntVar[]{left.getIntVar(), right.getIntVar()}, new int[]{1, -1}));
                return OrToolsType.intVar(var);
            case MulExpr:
                model.addProductEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return OrToolsType.intVar(var);
            case DivExpr:
                model.addDivisionEquality(var, left.getIntVar(), right.getIntVar());
                return OrToolsType.intVar(var);
            case MinExpr:
                model.addMinEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return OrToolsType.intVar(var);
            case MaxExpr:
                model.addMaxEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return OrToolsType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    public OrToolsType visitBoolVar(BoolVar boolVar) {
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
    public OrToolsType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        OrToolsType result = visit(expressionBoolVar.getExpression());
        IntVar var;
        if (result.isConstraint()) {
            var = constraintToVar(result).getVar();
        } else if (result.isIntVar()) {
            var = result.getIntVar();
        } else {
            throw new UnexpectedTypeException(expressionBoolVar.getExpression());
        }
        addBoolVar(name, var);
        return OrToolsType.intVar(var);
    }

    @Override
    public OrToolsType visitIntVar(nl.svenkonings.jacomo.variables.integer.IntVar intVar) {
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
    public OrToolsType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
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
