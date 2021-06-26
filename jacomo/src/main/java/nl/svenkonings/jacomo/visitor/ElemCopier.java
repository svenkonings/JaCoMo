package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.*;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.*;
import nl.svenkonings.jacomo.elem.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.InstantiatableBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.BoundedIntVar;
import nl.svenkonings.jacomo.elem.variables.integer.ConstantIntVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ElemCopier implements Visitor<Elem> {
    private final @NotNull Map<Elem, Elem> copyMap;

    public ElemCopier() {
        copyMap = new HashMap<>();
    }

    @Override
    public Elem visit(Elem elem) throws UnknownTypeException {
        if (copyMap.containsKey(elem)) {
            return copyMap.get(elem);
        } else {
            Elem copy = Visitor.super.visit(elem);
            copyMap.put(elem, copy);
            return copy;
        }
    }

    public void reset() {
        copyMap.clear();
    }

    @Override
    public Elem visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        return new BoolExprConstraint((BoolExpr) visit(boolExprConstraint.getExpr()));
    }

    @Override
    public Elem visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return new ConstantBoolExpr(constantBoolExpr.getValue());
    }

    @Override
    public Elem visitNotExpr(NotExpr notExpr) {
        return new NotExpr((BoolExpr) visit(notExpr.getExpr()));
    }

    @Override
    public Elem visitAndExpr(AndExpr andExpr) {
        return new AndExpr((BoolExpr) visit(andExpr.getLeft()), (BoolExpr) visit(andExpr.getRight()));
    }

    @Override
    public Elem visitOrExpr(OrExpr orExpr) {
        return new OrExpr((BoolExpr) visit(orExpr.getLeft()), (BoolExpr) visit(orExpr.getRight()));
    }

    @Override
    public Elem visitEqExpr(EqExpr eqExpr) {
        return new EqExpr((IntExpr) visit(eqExpr.getLeft()), (IntExpr) visit(eqExpr.getRight()));
    }

    @Override
    public Elem visitNeExpr(NeExpr neExpr) {
        return new NeExpr((IntExpr) visit(neExpr.getLeft()), (IntExpr) visit(neExpr.getRight()));
    }

    @Override
    public Elem visitGtExpr(GtExpr gtExpr) {
        return new GtExpr((IntExpr) visit(gtExpr.getLeft()), (IntExpr) visit(gtExpr.getRight()));
    }

    @Override
    public Elem visitGeExpr(GeExpr geExpr) {
        return new GeExpr((IntExpr) visit(geExpr.getLeft()), (IntExpr) visit(geExpr.getRight()));
    }

    @Override
    public Elem visitLtExpr(LtExpr ltExpr) {
        return new LtExpr((IntExpr) visit(ltExpr.getLeft()), (IntExpr) visit(ltExpr.getRight()));
    }

    @Override
    public Elem visitLeExpr(LeExpr leExpr) {
        return new LeExpr((IntExpr) visit(leExpr.getLeft()), (IntExpr) visit(leExpr.getRight()));
    }

    @Override
    public Elem visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return new ConstantIntExpr(constantIntExpr.getValue());
    }

    @Override
    public Elem visitAddExpr(AddExpr addExpr) {
        return new AddExpr((IntExpr) visit(addExpr.getLeft()), (IntExpr) visit(addExpr.getRight()));
    }

    @Override
    public Elem visitSubExpr(SubExpr subExpr) {
        return new SubExpr((IntExpr) visit(subExpr.getLeft()), (IntExpr) visit(subExpr.getRight()));
    }

    @Override
    public Elem visitMulExpr(MulExpr mulExpr) {
        return new MulExpr((IntExpr) visit(mulExpr.getLeft()), (IntExpr) visit(mulExpr.getRight()));
    }

    @Override
    public Elem visitDivExpr(DivExpr divExpr) {
        return new DivExpr((IntExpr) visit(divExpr.getLeft()), (IntExpr) visit(divExpr.getRight()));
    }

    @Override
    public Elem visitMinExpr(MinExpr minExpr) {
        return new MinExpr((IntExpr) visit(minExpr.getLeft()), (IntExpr) visit(minExpr.getRight()));
    }

    @Override
    public Elem visitMaxExpr(MaxExpr maxExpr) {
        return new MaxExpr((IntExpr) visit(maxExpr.getLeft()), (IntExpr) visit(maxExpr.getRight()));
    }

    @Override
    public Elem visitConstantBoolVar(ConstantBoolVar constantBoolVar) {
        return new ConstantBoolVar(constantBoolVar.getName(), constantBoolVar.getValue());
    }

    @Override
    public Elem visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        return new ExpressionBoolVar(expressionBoolVar.getName(), (BoolExpr) visit(expressionBoolVar.getExpression()));
    }

    @Override
    public Elem visitInstantiatableBoolVar(InstantiatableBoolVar instantiatableBoolVar) {
        return new InstantiatableBoolVar(instantiatableBoolVar.getName(), instantiatableBoolVar.getValue());
    }

    @Override
    public Elem visitConstantIntVar(ConstantIntVar constantIntVar) {
        return new ConstantIntVar(constantIntVar.getName(), constantIntVar.getValue());
    }

    @Override
    public Elem visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        return new ExpressionIntVar(expressionIntVar.getName(), (IntExpr) visit(expressionIntVar.getExpression()));
    }

    @Override
    public Elem visitBoundedIntVar(BoundedIntVar boundedIntVar) {
        return new BoundedIntVar(boundedIntVar.getName(), boundedIntVar.getLowerBound(), boundedIntVar.getUpperBound());
    }
}
