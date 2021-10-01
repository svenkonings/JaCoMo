/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.*;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.*;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"ConstantConditions", "IntegerDivisionInFloatingPointContext"})
public class OrToolsMpVisitor implements Visitor<OrToolsMpType> {

    public static double MAX_INT_BOUND = Integer.MAX_VALUE / 1000;
    public static double MIN_INT_BOUND = Integer.MIN_VALUE / 1000;
    public static double BIG_M = MAX_INT_BOUND - MIN_INT_BOUND;

    private final @NotNull MPSolver solver;

    private final @NotNull Map<String, MPVariable> boolVars;
    private final @NotNull Map<String, MPVariable> realVars;

    private final @NotNull List<MPVariable> minimizeVars;
    private final @NotNull List<MPVariable> maximizeVars;

    private final @NotNull Map<Elem, MPBool> boolMap;
    private final @NotNull Map<Elem, MPReal> realMap;

    private int genNameCounter;

    public OrToolsMpVisitor() {
        Loader.loadNativeLibraries();
        solver = MPSolver.createSolver("SCIP");
        boolVars = new LinkedHashMap<>();
        realVars = new LinkedHashMap<>();
        minimizeVars = new ArrayList<>();
        maximizeVars = new ArrayList<>();
        boolMap = new HashMap<>();
        realMap = new HashMap<>();
        genNameCounter = 0;
    }

    public @NotNull MPSolver getSolver() {
        return solver;
    }

    private void addBoolVar(String name, MPVariable var) {
        if (boolVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        } else if (realVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, realVars.get(name), var);
        }
        boolVars.put(name, var);
    }

    public Map<String, MPVariable> getBoolVars() {
        return boolVars;
    }

    private void addRealVar(String name, MPVariable var) {
        if (realVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, realVars.get(name), var);
        } else if (boolVars.containsKey(name)) {
            throw new DuplicateNameException("Variable name %s already exists. Var1: %s, Var2: %s", name, boolVars.get(name), var);
        }
        realVars.put(name, var);
    }

    public Map<String, MPVariable> getRealVars() {
        return realVars;
    }

    public List<MPVariable> getMinimizeVars() {
        return minimizeVars;
    }

    public List<MPVariable> getMaximizeVars() {
        return maximizeVars;
    }

    private String genName() {
        return "_linear" + genNameCounter++;
    }

    private MPVariable genBoolVar() {
        return solver.makeBoolVar(genName());
    }

    private MPVariable genRealVar() {
        return solver.makeIntVar(MIN_INT_BOUND, MAX_INT_BOUND, genName());
    }

    private MPBool bool(Elem elem) {
        if (boolMap.containsKey(elem)) {
            return boolMap.get(elem);
        }
        OrToolsMpType result = visit(elem);
        MPBool bool;
        if (result.isBool()) {
            bool = result.getBool();
        } else {
            throw new UnexpectedTypeException(elem);
        }
        boolMap.put(elem, bool);
        return bool;
    }

    private MPVariable boolToVar(MPBool bool) {
        if (bool.hasVariable()) {
            return bool.getVariable();
        } else if (bool.isConstant()) {
            double constant = bool.getConstant() ? 1.0 : 0.0;
            MPVariable boolVar = solver.makeIntVar(constant, constant, genName());
            bool.setVariable(boolVar);
            return boolVar;
        } else {
            throw new UnknownTypeException("Bool to variable conversion failed");
        }
    }

    private MPReal real(Elem elem) {
        if (realMap.containsKey(elem)) {
            return realMap.get(elem);
        }
        OrToolsMpType result = visit(elem);
        MPReal realVec;
        if (result.isReal()) {
            realVec = result.getReal();
        } else {
            throw new UnexpectedTypeException(elem);
        }
        realMap.put(elem, realVec);
        return realVec;
    }

    private MPVariable realToVar(MPReal real) {
        if (real.hasVariable()) {
            return real.getVariable();
        } else if (real.isConstant()) {
            MPVariable realVar = solver.makeNumVar(real.getConstant(), real.getConstant(), genName());
            real.setVariable(realVar);
            return realVar;
        } else if (real.isVector()) {
            MPConstraint constraint = solver.makeConstraint(-real.getConstant(), -real.getConstant());
            for (MPReal.Term term : real.getTerms()) {
                constraint.setCoefficient(term.getVariable(), term.getCoefficient());
            }
            MPVariable realVar = genRealVar();
            constraint.setCoefficient(realVar, -1);
            real.setVariable(realVar);
            return realVar;
        } else {
            throw new UnknownTypeException("Real to variable conversion failed");
        }
    }

    @Override
    public OrToolsMpType visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        MPBool bool = bool(boolExprConstraint.getExpr());
        MPVariable boolVar = boolToVar(bool);
        MPConstraint constraint = solver.makeConstraint(1.0, 1.0);
        constraint.setCoefficient(boolVar, 1.0);
        return OrToolsMpType.none();
    }

    @Override
    public OrToolsMpType visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return OrToolsMpType.bool(new MPBool(constantBoolExpr.getValue()));
    }

    @Override
    public OrToolsMpType visitNotExpr(NotExpr notExpr) {
        MPBool expr = bool(notExpr.getExpr());
        MPBool result;
        if (expr.isConstant()) {
            result = new MPBool(!expr.getConstant());
        } else {
            MPVariable boolVar = genBoolVar();
            MPVariable exprVar = expr.getVariable();
            MPConstraint constraint = solver.makeConstraint(1.0, 1.0);
            constraint.setCoefficient(boolVar, 1.0);
            constraint.setCoefficient(exprVar, 1.0);
            result = new MPBool(boolVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitAndExpr(AndExpr andExpr) {
        MPBool left = bool(andExpr.getLeft());
        MPBool right = bool(andExpr.getRight());
        MPBool result;
        if (left.isConstant()) {
            result = left.getConstant() ? right : left;
        } else if (right.isConstant()) {
            result = right.getConstant() ? left : right;
        } else {
            // TODO: Confirm if triple constraint is more efficient than single constraint (due too LP relaxation)
            MPVariable leftVar = left.getVariable();
            MPVariable rightVar = right.getVariable();
            MPVariable resultVar = genBoolVar();
            // If both are true the result must be true
            MPConstraint positiveConstraint = solver.makeConstraint(0.0, 1.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, 1.0);
            positiveConstraint.setCoefficient(resultVar, -1.0);
            // If left is false the result must be false
            MPConstraint negativeConstraintLeft = solver.makeConstraint(0.0, 1.0);
            negativeConstraintLeft.setCoefficient(leftVar, 1.0);
            negativeConstraintLeft.setCoefficient(resultVar, -1.0);
            // If right is false the result must be false
            MPConstraint negativeConstraintRight = solver.makeConstraint(0.0, 1.0);
            negativeConstraintRight.setCoefficient(rightVar, 1.0);
            negativeConstraintRight.setCoefficient(resultVar, -1.0);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitOrExpr(OrExpr orExpr) {
        MPBool left = bool(orExpr.getLeft());
        MPBool right = bool(orExpr.getRight());
        MPBool result;
        if (left.isConstant()) {
            result = left.getConstant() ? left : right;
        } else if (right.isConstant()) {
            result = right.getConstant() ? right : left;
        } else {
            // TODO: Confirm if triple constraint is more efficient than single constraint (due too LP relaxation)
            MPVariable leftVar = left.getVariable();
            MPVariable rightVar = right.getVariable();
            MPVariable resultVar = genBoolVar();
            // If both are false the result must be false
            MPConstraint negativeConstraint = solver.makeConstraint(0.0, 1.0);
            negativeConstraint.setCoefficient(leftVar, 1.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(resultVar, -1.0);
            // If left is true the result must be true
            MPConstraint positiveConstraintLeft = solver.makeConstraint(-1.0, 0.0);
            positiveConstraintLeft.setCoefficient(leftVar, 1.0);
            positiveConstraintLeft.setCoefficient(resultVar, -1.0);
            // If right is true the result must be true
            MPConstraint positiveConstraintRight = solver.makeConstraint(-1.0, 0.0);
            positiveConstraintRight.setCoefficient(rightVar, 1.0);
            positiveConstraintRight.setCoefficient(resultVar, -1.0);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitEqExpr(EqExpr eqExpr) {
        MPReal left = real(eqExpr.getLeft());
        MPReal right = real(eqExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() == right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            MPVariable inverseVar = genBoolVar();
            MPConstraint inverseConstraint = solver.makeConstraint(1.0, 1.0);
            inverseConstraint.setCoefficient(resultVar, 1.0);
            inverseConstraint.setCoefficient(inverseVar, 1.0);
            // If left is larger than right the result must be false
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, 0.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(inverseVar, -BIG_M);
            // If right is larger than the result must be false
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, 0.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(inverseVar, -BIG_M);
            // Maximize result
            maximizeVars.add(resultVar);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitNeExpr(NeExpr neExpr) {
        MPReal left = real(neExpr.getLeft());
        MPReal right = real(neExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() == right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            // If left is larger than right the result must be true
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, -1.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(resultVar, -BIG_M);
            // If right is larger than the result must be true
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, -1.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(resultVar, -BIG_M);
            // Minimize result
            minimizeVars.add(resultVar);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitGtExpr(GtExpr gtExpr) {
        MPReal left = real(gtExpr.getLeft());
        MPReal right = real(gtExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() > right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            MPVariable inverseVar = genBoolVar();
            MPConstraint inverseConstraint = solver.makeConstraint(1.0, 1.0);
            inverseConstraint.setCoefficient(resultVar, 1.0);
            inverseConstraint.setCoefficient(inverseVar, 1.0);
            // If left is larger than right the result must be true
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, 0.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(resultVar, -BIG_M);
            // If right is larger or equal to left the result must be false
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, 1.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(inverseVar, -BIG_M);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitGeExpr(GeExpr geExpr) {
        MPReal left = real(geExpr.getLeft());
        MPReal right = real(geExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() >= right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            MPVariable inverseVar = genBoolVar();
            MPConstraint inverseConstraint = solver.makeConstraint(1.0, 1.0);
            inverseConstraint.setCoefficient(resultVar, 1.0);
            inverseConstraint.setCoefficient(inverseVar, 1.0);
            // If left is larger or equal to right the result must be true
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, 1.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(resultVar, -BIG_M);
            // If right is larger than left the result must be false
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, 0.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(inverseVar, -BIG_M);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitLtExpr(LtExpr ltExpr) {
        MPReal left = real(ltExpr.getLeft());
        MPReal right = real(ltExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() < right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            MPVariable inverseVar = genBoolVar();
            MPConstraint inverseConstraint = solver.makeConstraint(1.0, 1.0);
            inverseConstraint.setCoefficient(resultVar, 1.0);
            inverseConstraint.setCoefficient(inverseVar, 1.0);
            // If left is larger or equal to right the result must be false
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, 1.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(inverseVar, -BIG_M);
            // If right is larger than left the result must be true
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, 0.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(resultVar, -BIG_M);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitLeExpr(LeExpr leExpr) {
        MPReal left = real(leExpr.getLeft());
        MPReal right = real(leExpr.getRight());
        MPBool result;
        if (left.isConstant() && right.isConstant()) {
            result = new MPBool(left.getConstant() <= right.getConstant());
        } else {
            MPVariable leftVar = realToVar(left);
            MPVariable rightVar = realToVar(right);
            MPVariable resultVar = genBoolVar();
            MPVariable inverseVar = genBoolVar();
            MPConstraint inverseConstraint = solver.makeConstraint(1.0, 1.0);
            inverseConstraint.setCoefficient(resultVar, 1.0);
            inverseConstraint.setCoefficient(inverseVar, 1.0);
            // If left is larger than right the result must be false
            MPConstraint positiveConstraint = solver.makeConstraint(-BIG_M, 0.0);
            positiveConstraint.setCoefficient(leftVar, 1.0);
            positiveConstraint.setCoefficient(rightVar, -1.0);
            positiveConstraint.setCoefficient(inverseVar, -BIG_M);
            // If right is larger or equal to left the result must be true
            MPConstraint negativeConstraint = solver.makeConstraint(-BIG_M, 1.0);
            negativeConstraint.setCoefficient(rightVar, 1.0);
            negativeConstraint.setCoefficient(leftVar, -1.0);
            negativeConstraint.setCoefficient(resultVar, -BIG_M);
            result = new MPBool(resultVar);
        }
        return OrToolsMpType.bool(result);
    }

    @Override
    public OrToolsMpType visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return OrToolsMpType.real(new MPReal(constantIntExpr.getValue()));
    }

    @Override
    public OrToolsMpType visitAddExpr(AddExpr addExpr) {
        MPReal left = real(addExpr.getLeft());
        MPReal right = real(addExpr.getRight());
        MPReal result = left.add(right.getTerms(), right.getConstant());
        return OrToolsMpType.real(result);
    }

    @Override
    public OrToolsMpType visitSubExpr(SubExpr subExpr) {
        MPReal left = real(subExpr.getLeft());
        MPReal right = real(subExpr.getRight());
        List<MPReal.Term> negTerms = right.getTerms().stream()
                .map(term -> term.withCoefficient(-term.getCoefficient()))
                .collect(Collectors.toList());
        MPReal result = left.add(negTerms, -right.getConstant());
        return OrToolsMpType.real(result);
    }

    @Override
    public OrToolsMpType visitMulExpr(MulExpr mulExpr) {
        MPReal left = real(mulExpr.getLeft());
        MPReal right = real(mulExpr.getRight());
        if (right.isConstant()) {
            return OrToolsMpType.real(mul(left, right.getConstant()));
        } else if (left.isConstant()) {
            return OrToolsMpType.real(mul(right, left.getConstant()));
        } else {
            throw new UnexpectedTypeException("Non-linear multiplication detected. OR-TOOLS MP Solver only supports linear expressions, try using OR-TOOLS CP-SAT Solver instead.");
        }
    }

    private MPReal mul(MPReal real, double multiplicator) {
        if (multiplicator == 0.0) {
            return new MPReal(0.0);
        }
        List<MPReal.Term> terms = real.getTerms().stream()
                .map(term -> term.withCoefficient(term.getCoefficient() * multiplicator))
                .collect(Collectors.toList());
        double constant = real.getConstant() * multiplicator;
        return new MPReal(terms, constant);
    }

    @Override
    public OrToolsMpType visitDivExpr(DivExpr divExpr) {
        MPReal left = real(divExpr.getLeft());
        MPReal right = real(divExpr.getRight());
        if (right.isConstant()) {
            return OrToolsMpType.real(div(left, right.getConstant()));
        } else {
            throw new UnexpectedTypeException("Non-linear division detected. OR-TOOLS MP Solver only supports linear expressions, try using OR-TOOLS CP-SAT Solver instead.");
        }
    }

    private MPReal div(MPReal real, double divisor) {
        if (divisor == 0.0) {
            throw new InvalidInputException("Can't divide by zero");
        }
        List<MPReal.Term> terms = real.getTerms().stream()
                .map(term -> term.withCoefficient(term.getCoefficient() / divisor))
                .collect(Collectors.toList());
        double constant = real.getConstant() / divisor;
        return new MPReal(terms, constant);
    }

    @Override
    public OrToolsMpType visitMinExpr(MinExpr minExpr) {
        MPReal left = real(minExpr.getLeft());
        MPReal right = real(minExpr.getRight());
        MPVariable realVar;
        if (left.isConstant() && right.isConstant()) {
            return OrToolsMpType.real(new MPReal(Math.min(left.getConstant(), right.getConstant())));
        } else if (left.isConstant() || right.isConstant()) {
            MPReal real = left.isConstant() ? right : left;
            double constant = left.isConstant() ? left.getConstant() : right.getConstant();
            realVar = solver.makeNumVar(MIN_INT_BOUND, constant, genName());
            MPVariable other = realToVar(real);
            MPConstraint constraint = solver.makeConstraint(0.0, BIG_M);
            constraint.setCoefficient(other, 1.0);
            constraint.setCoefficient(realVar, -1.0);
        } else {
            realVar = solver.makeNumVar(MIN_INT_BOUND, MAX_INT_BOUND, genName());
            MPVariable leftVar = realToVar(left);
            MPConstraint leftConstraint = solver.makeConstraint(0, BIG_M);
            leftConstraint.setCoefficient(leftVar, 1.0);
            leftConstraint.setCoefficient(realVar, -1.0);
            MPVariable rightVar = realToVar(right);
            MPConstraint rightConstraint = solver.makeConstraint(0, BIG_M);
            rightConstraint.setCoefficient(rightVar, 1.0);
            rightConstraint.setCoefficient(realVar, -1.0);
        }
        maximizeVars.add(realVar);
        return OrToolsMpType.real(new MPReal(realVar));
    }

    @Override
    public OrToolsMpType visitMaxExpr(MaxExpr maxExpr) {
        MPReal left = real(maxExpr.getLeft());
        MPReal right = real(maxExpr.getRight());
        MPVariable realVar;
        if (left.isConstant() && right.isConstant()) {
            return OrToolsMpType.real(new MPReal(Math.max(left.getConstant(), right.getConstant())));
        } else if (left.isConstant() || right.isConstant()) {
            MPReal real = left.isConstant() ? right : left;
            double constant = left.isConstant() ? left.getConstant() : right.getConstant();
            realVar = solver.makeNumVar(constant, MAX_INT_BOUND, genName());
            MPVariable other = realToVar(real);
            MPConstraint constraint = solver.makeConstraint(-BIG_M, 0);
            constraint.setCoefficient(other, 1.0);
            constraint.setCoefficient(realVar, -1.0);
        } else {
            realVar = solver.makeNumVar(MIN_INT_BOUND, MAX_INT_BOUND, genName());
            MPVariable leftVar = realToVar(left);
            MPConstraint leftConstraint = solver.makeConstraint(-BIG_M, 0);
            leftConstraint.setCoefficient(leftVar, 1.0);
            leftConstraint.setCoefficient(realVar, -1.0);
            MPVariable rightVar = realToVar(right);
            MPConstraint rightConstraint = solver.makeConstraint(-BIG_M, 0);
            rightConstraint.setCoefficient(rightVar, 1.0);
            rightConstraint.setCoefficient(realVar, -1.0);

        }
        minimizeVars.add(realVar);
        return OrToolsMpType.real(new MPReal(realVar));
    }

    @Override
    public OrToolsMpType visitBoolVar(BoolVar boolVar) {
        String name = boolVar.getName();
        MPBool bool;
        if (boolVars.containsKey(name)) {
            MPVariable var = boolVars.get(name);
            bool = new MPBool(var);
        } else {
            double lb = (boolVar.hasValue() && boolVar.getValue()) ? 1.0 : 0.0;
            double ub = (boolVar.hasValue() && !boolVar.getValue()) ? 0.0 : 1.0;
            MPVariable var = solver.makeIntVar(lb, ub, name);
            addBoolVar(name, var);
            bool = new MPBool(var);
        }
        return OrToolsMpType.bool(bool);
    }

    @Override
    public OrToolsMpType visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        String name = expressionBoolVar.getName();
        MPBool bool;
        if (boolVars.containsKey(name)) {
            MPVariable var = boolVars.get(name);
            bool = new MPBool(var);
        } else {
            bool = bool(expressionBoolVar.getExpression());
            MPVariable var = boolToVar(bool);
            addBoolVar(name, var);
        }
        return OrToolsMpType.bool(bool);
    }

    @Override
    public OrToolsMpType visitIntVar(IntVar intVar) {
        String name = intVar.getName();
        MPReal real;
        if (realVars.containsKey(name)) {
            MPVariable var = realVars.get(name);
            real = new MPReal(var);
        } else {
            double lb = intVar.hasLowerBound() ? intVar.getLowerBound() : MIN_INT_BOUND;
            double ub = intVar.hasUpperBound() ? intVar.getUpperBound() : MAX_INT_BOUND;
            MPVariable var = solver.makeNumVar(lb, ub, name);
            addRealVar(name, var);
            real = new MPReal(var);
        }
        return OrToolsMpType.real(real);
    }

    @Override
    public OrToolsMpType visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        String name = expressionIntVar.getName();
        MPReal real;
        if (realVars.containsKey(name)) {
            MPVariable var = realVars.get(name);
            real = new MPReal(var);
        } else {
            real = real(expressionIntVar.getExpression());
            MPVariable var = realToVar(real);
            addRealVar(name, var);
        }
        return OrToolsMpType.real(real);
    }
}
