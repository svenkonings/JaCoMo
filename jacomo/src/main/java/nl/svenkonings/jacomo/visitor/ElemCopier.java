package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.*;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.ConstantIntExpr;
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

/**
 * Visitor which copies elements.
 * Copied elements are cached and reused when a duplicate element is encountered.
 */
@SuppressWarnings("unchecked")
public class ElemCopier implements Visitor<Elem> {
    private final @NotNull Map<Elem, Elem> copyMap;

    /**
     * Creates a new ElemCopier.
     */
    public ElemCopier() {
        copyMap = new HashMap<>();
    }

    /**
     * Create a copy of the given element.
     * The copy will be cached and used when duplicate elements are encountered.
     *
     * @param elem the given element.
     * @param <T>  the type of the element.
     * @return the copied element.
     * @throws UnknownTypeException when the type of the element is not supported by this visitor.
     */
    public <T extends Elem> T copy(T elem) throws UnknownTypeException {
        if (copyMap.containsKey(elem)) {
            return (T) copyMap.get(elem);
        } else {
            T copy = (T) visit(elem);
            copyMap.put(elem, copy);
            return copy;
        }
    }

    /**
     * Reset the cache of copied elements.
     */
    public void reset() {
        copyMap.clear();
    }

    @Override
    public Elem visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        return new BoolExprConstraint(copy(boolExprConstraint.getExpr()));
    }

    @Override
    public Elem visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return new ConstantBoolExpr(constantBoolExpr.getValue());
    }

    @Override
    public Elem visitNotExpr(NotExpr notExpr) {
        return new NotExpr(copy(notExpr.getExpr()));
    }

    @Override
    public Elem visitAndExpr(AndExpr andExpr) {
        return new AndExpr(copy(andExpr.getLeft()), copy(andExpr.getRight()));
    }

    @Override
    public Elem visitOrExpr(OrExpr orExpr) {
        return new OrExpr(copy(orExpr.getLeft()), copy(orExpr.getRight()));
    }

    @Override
    public Elem visitEqExpr(EqExpr eqExpr) {
        return new EqExpr(copy(eqExpr.getLeft()), copy(eqExpr.getRight()));
    }

    @Override
    public Elem visitNeExpr(NeExpr neExpr) {
        return new NeExpr(copy(neExpr.getLeft()), copy(neExpr.getRight()));
    }

    @Override
    public Elem visitGtExpr(GtExpr gtExpr) {
        return new GtExpr(copy(gtExpr.getLeft()), copy(gtExpr.getRight()));
    }

    @Override
    public Elem visitGeExpr(GeExpr geExpr) {
        return new GeExpr(copy(geExpr.getLeft()), copy(geExpr.getRight()));
    }

    @Override
    public Elem visitLtExpr(LtExpr ltExpr) {
        return new LtExpr(copy(ltExpr.getLeft()), copy(ltExpr.getRight()));
    }

    @Override
    public Elem visitLeExpr(LeExpr leExpr) {
        return new LeExpr(copy(leExpr.getLeft()), copy(leExpr.getRight()));
    }

    @Override
    public Elem visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return new ConstantIntExpr(constantIntExpr.getValue());
    }

    @Override
    public Elem visitAddExpr(AddExpr addExpr) {
        return new AddExpr(copy(addExpr.getLeft()), copy(addExpr.getRight()));
    }

    @Override
    public Elem visitSubExpr(SubExpr subExpr) {
        return new SubExpr(copy(subExpr.getLeft()), copy(subExpr.getRight()));
    }

    @Override
    public Elem visitMulExpr(MulExpr mulExpr) {
        return new MulExpr(copy(mulExpr.getLeft()), copy(mulExpr.getRight()));
    }

    @Override
    public Elem visitDivExpr(DivExpr divExpr) {
        return new DivExpr(copy(divExpr.getLeft()), copy(divExpr.getRight()));
    }

    @Override
    public Elem visitMinExpr(MinExpr minExpr) {
        return new MinExpr(copy(minExpr.getLeft()), copy(minExpr.getRight()));
    }

    @Override
    public Elem visitMaxExpr(MaxExpr maxExpr) {
        return new MaxExpr(copy(maxExpr.getLeft()), copy(maxExpr.getRight()));
    }

    @Override
    public Elem visitConstantBoolVar(ConstantBoolVar constantBoolVar) {
        return new ConstantBoolVar(constantBoolVar.getName(), constantBoolVar.getValue());
    }

    @Override
    public Elem visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        return new ExpressionBoolVar(expressionBoolVar.getName(), copy(expressionBoolVar.getExpression()));
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
        return new ExpressionIntVar(expressionIntVar.getName(), copy(expressionIntVar.getExpression()));
    }

    @Override
    public Elem visitBoundedIntVar(BoundedIntVar boundedIntVar) {
        return new BoundedIntVar(boundedIntVar.getName(), boundedIntVar.getLowerBound(), boundedIntVar.getUpperBound());
    }
}
