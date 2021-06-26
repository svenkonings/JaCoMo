package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.util.Domain;
import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.BiExpr;
import nl.svenkonings.jacomo.elem.expressions.Expr;
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
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Visitor which builds a OR-Tools CP-SAT model from the visited elements.
 */
@SuppressWarnings("ConstantConditions")
public class OrToolsVisitor implements Visitor<OrToolsType> {

    private final @NotNull CpModel model;

    private final @NotNull Map<String, IntVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;

    private final @NotNull Map<Elem, Constraint> constraintMap;
    private final @NotNull Map<Elem, IntVar> intVarMap;

    private int genNameCounter;
    private final @NotNull List<IntVar> inverseVars;

    /**
     * Create a new OR-Tools visitor.
     */
    public OrToolsVisitor() {
        OrToolsLoader.loadLibrary();
        model = new CpModel();
        boolVars = new HashMap<>();
        intVars = new HashMap<>();
        constraintMap = new HashMap<>();
        intVarMap = new HashMap<>();
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

    private Constraint constraint(Elem elem) {
        if (constraintMap.containsKey(elem)) {
            return constraintMap.get(elem);
        }
        OrToolsType result = visit(elem);
        Constraint constraint;
        if (result.isConstraint()) {
            constraint = result.getConstraint();
        } else if (result.isIntVar()) {
            IntVar var = result.getIntVar();
            Domain domain = var.getDomain();
            if (domain.min() == 0L && domain.max() == 1L) {
                // Assume the variable is a boolean, should be true (1)
                constraint = model.addEquality(result.getIntVar(), 1);
            } else {
                throw new UnexpectedTypeException(elem);
            }
        } else {
            throw new UnexpectedTypeException(elem);
        }
        constraintMap.put(elem, constraint);
        return constraint;
    }

    private IntVar intVar(Elem elem, boolean convertConstraint) {
        if (intVarMap.containsKey(elem)) {
            return intVarMap.get(elem);
        }
        OrToolsType result = visit(elem);
        IntVar intVar;
        if (result.isIntVar()) {
            intVar = result.getIntVar();
        } else if (convertConstraint && result.isConstraint()) {
            IntVar var = genBoolVar();
            result.getConstraint().onlyEnforceIf(var);
            IntVar inverseVar = getInverse(var);
            result.getInverseConstraint().onlyEnforceIf(inverseVar);
            intVar = var;
        } else {
            throw new UnexpectedTypeException(elem);
        }
        intVarMap.put(elem, intVar);
        return intVar;
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
            vars.add(intVar(child, true));
        }
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
        IntVar var = getInverse(intVar(notExpr.getExpr(), true));
        return OrToolsType.intVar(var);
    }

    @Override
    public OrToolsType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
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
    public OrToolsType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        IntVar left = intVar(reBoolExpr.getLeft(), false);
        IntVar right = intVar(reBoolExpr.getRight(), false);
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return OrToolsType.constraint(model.addEquality(left, right),
                        () -> model.addDifferent(left, right));
            case NeExpr:
                return OrToolsType.constraint(model.addDifferent(left, right),
                        () -> model.addEquality(left, right));
            case GtExpr:
                return OrToolsType.constraint(model.addGreaterThan(left, right),
                        () -> model.addLessOrEqual(left, right));
            case GeExpr:
                return OrToolsType.constraint(model.addGreaterOrEqual(left, right),
                        () -> model.addLessThan(left, right));
            case LtExpr:
                return OrToolsType.constraint(model.addLessThan(left, right),
                        () -> model.addGreaterOrEqual(left, right));
            case LeExpr:
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
        IntVar left = intVar(biIntExpr.getLeft(), false);
        IntVar right = intVar(biIntExpr.getRight(), false);
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case SubExpr:
                // OPTIMIZATION: Combine multiple subtraction scalar expressions
                model.addEquality(var, LinearExpr.scalProd(new IntVar[]{left, right}, new int[]{1, -1}));
                return OrToolsType.intVar(var);
            case MulExpr:
                model.addProductEquality(var, new IntVar[]{left, right});
                return OrToolsType.intVar(var);
            case DivExpr:
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
    public OrToolsType visitBoolVar(BoolVar boolVar) {
        String name = boolVar.getName();
        IntVar var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            if (boolVar.hasValue()) {
                int value = boolVar.getValue() ? 1 : 0;
                var = model.newConstant(value);
            } else {
                var = model.newBoolVar(name);
            }
            addBoolVar(name, var);
        }
        return OrToolsType.intVar(var);
    }

    @Override
    public OrToolsType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        IntVar var;
        if (boolVars.containsKey(name)) {
            var = boolVars.get(name);
        } else {
            var = intVar(expressionBoolVar.getExpression(), true);
            addBoolVar(name, var);
        }
        return OrToolsType.intVar(var);
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
            var = intVar(expressionIntVar.getExpression(), false);
            addIntVar(name, var);
        }
        return OrToolsType.intVar(var);
    }
}
