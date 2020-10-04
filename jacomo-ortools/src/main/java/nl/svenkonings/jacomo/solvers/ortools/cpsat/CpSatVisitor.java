package nl.svenkonings.jacomo.solvers.ortools.cpsat;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import com.google.ortools.util.Domain;
import com.skaggsm.ortools.OrToolsHelper;
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
 * Visitor which builds a CP-SAT model from the visited elements.
 */
@SuppressWarnings("ConstantConditions")
public class CpSatVisitor extends Visitor<CpSatType> {
    static {
        OrToolsHelper.loadLibrary();
    }

    private final @NotNull CpModel model;
    private final @NotNull Map<String, IntVar> boolVars;
    private final @NotNull Map<String, IntVar> intVars;
    private int genNameCounter;

    /**
     * Create a new CP-SAT visitor.
     */
    public CpSatVisitor() {
        super();
        model = new CpModel();
        boolVars = new LinkedHashMap<>();
        intVars = new LinkedHashMap<>();
        genNameCounter = 0;
    }

    /**
     * Returns the CP-SAT model.
     *
     * @return the CP-SAT model
     */
    public @NotNull CpModel getModel() {
        return model;
    }

    /**
     * Returns the mapping of boolean variable names to CP-SAT variables.
     *
     * @return the mapping of boolean variable names to CP-SAT variables
     */
    public @NotNull Map<String, IntVar> getBoolVars() {
        return boolVars;
    }

    /**
     * Returns the mapping of integer variable names to CP-SAT variables.
     *
     * @return the mapping of integer variable names to CP-SAT variables
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
        return model.newIntVarFromDomain(Domain.allValues(), genName());
    }

    @Override
    public CpSatType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        visit(boolExprConstraint.getExpr());
        return CpSatType.none();
    }

    @Override
    public CpSatType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        int value = constantBoolExpr.getValue() ? 1 : 0;
        return CpSatType.intVar(model.newConstant(value));
    }

    @Override
    public CpSatType visitNotExpr(NotExpr notExpr) {
        CpSatType result = visit(notExpr.getExpr());
        IntVar var;
        if (result.isConstraint()) {
            var = genBoolVar();
            result.getConstraint().onlyEnforceIf(var);
        } else if (result.isIntVar()) {
            var = result.getIntVar();
        } else {
            throw new UnexpectedTypeException(notExpr.getExpr());
        }
        IntVar inverseVar = genIntVar();
        try {
            model.addInverse(new IntVar[]{var}, new IntVar[]{inverseVar});
        } catch (CpModel.MismatchedArrayLengths e) {
            throw new UnsupportedOperationException(e);
        }
        return CpSatType.intVar(inverseVar);
    }

    @Override
    public CpSatType visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        CpSatType left = visit(biBoolExpr.getLeft());
        CpSatType right = visit(biBoolExpr.getRight());
        IntVar leftVar;
        IntVar rightVar;
        if (left.isConstraint()) {
            leftVar = genBoolVar();
            left.getConstraint().onlyEnforceIf(leftVar);
        } else if (left.isIntVar()) {
            leftVar = left.getIntVar();
        } else {
            throw new UnexpectedTypeException(biBoolExpr.getLeft());
        }
        if (right.isConstraint()) {
            rightVar = genBoolVar();
            right.getConstraint().onlyEnforceIf(rightVar);
        } else if (right.isIntVar()) {
            rightVar = right.getIntVar();
        } else {
            throw new UnexpectedTypeException(biBoolExpr.getLeft());
        }
        switch (biBoolExpr.getType()) {
            case AndExpr:
                return CpSatType.constraint(model.addBoolAnd(new Literal[]{leftVar, rightVar}));
            case OrExpr:
                return CpSatType.constraint(model.addBoolOr(new Literal[]{leftVar, rightVar}));
            default:
                throw new UnexpectedTypeException(biBoolExpr);
        }
    }

    @Override
    public CpSatType visitReBoolExpr(ReBoolExpr reBoolExpr) {
        CpSatType left = visit(reBoolExpr.getLeft());
        CpSatType right = visit(reBoolExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(reBoolExpr.getRight());
        }
        switch (reBoolExpr.getType()) {
            case EqExpr:
                return CpSatType.constraint(model.addEquality(left.getIntVar(), right.getIntVar()));
            case NeExpr:
                return CpSatType.constraint(model.addDifferent(left.getIntVar(), right.getIntVar()));
            case GtExpr:
                return CpSatType.constraint(model.addGreaterThan(left.getIntVar(), right.getIntVar()));
            case GeExpr:
                return CpSatType.constraint(model.addGreaterOrEqual(left.getIntVar(), right.getIntVar()));
            case LtExpr:
                return CpSatType.constraint(model.addLessThan(left.getIntVar(), right.getIntVar()));
            case LeExpr:
                return CpSatType.constraint(model.addLessOrEqual(left.getIntVar(), right.getIntVar()));
            default:
                throw new UnexpectedTypeException(reBoolExpr);
        }
    }

    @Override
    public CpSatType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return CpSatType.intVar(model.newConstant(constantIntExpr.getValue()));
    }

    @Override
    public CpSatType visitBiIntExpr(BiIntExpr biIntExpr) {
        CpSatType left = visit(biIntExpr.getLeft());
        CpSatType right = visit(biIntExpr.getRight());
        if (!left.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getLeft());
        } else if (!right.isIntVar()) {
            throw new UnexpectedTypeException(biIntExpr.getRight());
        }
        IntVar var = genIntVar();
        switch (biIntExpr.getType()) {
            case AddExpr:
                model.addEquality(var, LinearExpr.sum(new IntVar[]{left.getIntVar(), right.getIntVar()}));
                return CpSatType.intVar(var);
            case SubExpr:
                model.addEquality(var, LinearExpr.scalProd(new IntVar[]{left.getIntVar(), right.getIntVar()}, new int[]{1, -1}));
                return CpSatType.intVar(var);
            case MulExpr:
                model.addProductEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return CpSatType.intVar(var);
            case DivExpr:
                model.addDivisionEquality(var, left.getIntVar(), right.getIntVar());
                return CpSatType.intVar(var);
            case MinExpr:
                model.addMinEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return CpSatType.intVar(var);
            case MaxExpr:
                model.addMaxEquality(var, new IntVar[]{left.getIntVar(), right.getIntVar()});
                return CpSatType.intVar(var);
            default:
                throw new UnexpectedTypeException(biIntExpr);
        }
    }

    @Override
    public CpSatType visitBoolVar(BoolVar boolVar) {
        String name = boolVar.getName();
        IntVar var;
        if (boolVar.hasValue()) {
            int value = boolVar.getValue() ? 1 : 0;
            var = model.newConstant(value);
        } else {
            var = model.newBoolVar(name);
        }
        addBoolVar(name, var);
        return CpSatType.intVar(var);
    }

    @Override
    public CpSatType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        CpSatType result = visit(expressionBoolVar.getExpression());
        IntVar var;
        if (result.isConstraint()) {
            var = model.newBoolVar(name);
            result.getConstraint().onlyEnforceIf(var);
        } else if (result.isIntVar()) {
            var = result.getIntVar();
        } else {
            throw new UnexpectedTypeException(expressionBoolVar.getExpression());
        }
        addBoolVar(name, var);
        return CpSatType.intVar(var);
    }

    @Override
    public CpSatType visitIntVar(nl.svenkonings.jacomo.variables.integer.IntVar intVar) {
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
        return CpSatType.intVar(var);
    }

    @Override
    public CpSatType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        CpSatType result = visit(expressionIntVar.getExpression());
        if (!result.isIntVar()) {
            throw new UnexpectedTypeException(expressionIntVar.getExpression());
        }
        IntVar var = result.getIntVar();
        addIntVar(name, var);
        return CpSatType.intVar(var);
    }
}
