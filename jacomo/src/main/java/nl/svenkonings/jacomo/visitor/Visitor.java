package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.unchecked.NotImplementedException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import nl.svenkonings.jacomo.expressions.BiExpr;
import nl.svenkonings.jacomo.expressions.Expr;
import nl.svenkonings.jacomo.expressions.UnExpr;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.*;
import nl.svenkonings.jacomo.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.expressions.bool.unary.UnBoolExpr;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.*;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.*;
import nl.svenkonings.jacomo.variables.integer.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A visitor to traverse elements in a model.
 * Used to transform JaCoMo models to solver-specific models.
 *
 * @param <T> The return type of this visitor
 */
@SuppressWarnings("ConstantConditions")
public abstract class Visitor<T> {
    protected final Map<Elem, T> visited;

    protected Visitor() {
        visited = new HashMap<>();
    }

    // Elements
    protected T visitElem(Elem elem) {
        throw new NotImplementedException("Element type not supported by this visitor: %s", elem.getType());
    }

    // Constraints
    protected T visitConstraint(Constraint constraint) {
        return visitElem(constraint);
    }

    protected T visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        return visitConstraint(boolExprConstraint);
    }

    // Expressions
    protected T visitExpr(Expr expr) {
        return visitElem(expr);
    }

    protected T visitUnExpr(UnExpr unExpr) {
        return visitExpr(unExpr);
    }

    protected T visitBiExpr(BiExpr biExpr) {
        return visitExpr(biExpr);
    }

    // Bool expressions
    protected T visitBoolExpr(BoolExpr boolExpr) {
        return visitExpr(boolExpr);
    }

    protected T visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return visitBoolExpr(constantBoolExpr);
    }

    // Unary bool expressions
    protected T visitUnBoolExpr(UnBoolExpr unBoolExpr) {
        return visitBoolExpr(unBoolExpr);
    }

    protected T visitNotExpr(NotExpr notExpr) {
        return visitUnBoolExpr(notExpr);
    }

    // Binary bool expressions
    protected T visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        return visitBoolExpr(biBoolExpr);
    }

    protected T visitAndExpr(AndExpr andExpr) {
        return visitBiBoolExpr(andExpr);
    }

    protected T visitOrExpr(OrExpr orExpr) {
        return visitBiBoolExpr(orExpr);
    }

    // Relational bool expressions
    protected T visitReBoolExpr(ReBoolExpr reBoolExpr) {
        return visitBoolExpr(reBoolExpr);
    }

    protected T visitEqExpr(EqExpr eqExpr) {
        return visitReBoolExpr(eqExpr);
    }

    protected T visitNeExpr(NeExpr neExpr) {
        return visitReBoolExpr(neExpr);
    }

    protected T visitGtExpr(GtExpr gtExpr) {
        return visitReBoolExpr(gtExpr);
    }

    protected T visitGeExpr(GeExpr geExpr) {
        return visitReBoolExpr(geExpr);
    }

    protected T visitLtExpr(LtExpr ltExpr) {
        return visitReBoolExpr(ltExpr);
    }

    protected T visitLeExpr(LeExpr leExpr) {
        return visitReBoolExpr(leExpr);
    }

    // Int expressions
    protected T visitIntExpr(IntExpr intExpr) {
        return visitExpr(intExpr);
    }

    protected T visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return visitIntExpr(constantIntExpr);
    }

    // Binary int expressions
    protected T visitBiIntExpr(BiIntExpr biIntExpr) {
        return visitIntExpr(biIntExpr);
    }

    protected T visitAddExpr(AddExpr addExpr) {
        return visitBiIntExpr(addExpr);
    }

    protected T visitSubExpr(SubExpr subExpr) {
        return visitBiIntExpr(subExpr);
    }

    protected T visitMulExpr(MulExpr mulExpr) {
        return visitBiIntExpr(mulExpr);
    }

    protected T visitDivExpr(DivExpr divExpr) {
        return visitBiIntExpr(divExpr);
    }

    protected T visitMinExpr(MinExpr minExpr) {
        return visitBiIntExpr(minExpr);
    }

    protected T visitMaxExpr(MaxExpr maxExpr) {
        return visitBiIntExpr(maxExpr);
    }

    // Variables
    protected T visitVar(Var var) {
        return visitElem(var);
    }

    // Bool variables
    protected T visitBoolVar(BoolVar boolVar) {
        return visitVar(boolVar);
    }

    protected T visitConstantBoolVar(ConstantBoolVar constantBoolVar) {
        return visitBoolVar(constantBoolVar);
    }

    protected T visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        return visitBoolVar(expressionBoolVar);
    }

    protected T visitUpdatableBoolVar(UpdatableBoolVar updatableBoolVar) {
        return visitBoolVar(updatableBoolVar);
    }

    protected T visitInstantiatableBoolVar(InstantiatableBoolVar instantiatableBoolVar) {
        return visitUpdatableBoolVar(instantiatableBoolVar);
    }

    // Int variables
    protected T visitIntVar(IntVar intVar) {
        return visitVar(intVar);
    }

    protected T visitConstantIntVar(ConstantIntVar constantIntVar) {
        return visitIntVar(constantIntVar);
    }

    protected T visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        return visitIntVar(expressionIntVar);
    }

    protected T visitUpdatableIntVar(UpdatableIntVar updatableIntVar) {
        return visitIntVar(updatableIntVar);
    }

    protected T visitBoundedIntVar(BoundedIntVar boundedIntVar) {
        return visitUpdatableIntVar(boundedIntVar);
    }

    /**
     * Visits the specified element. This method will select which visit method
     * to use based on the result of {@link Elem#getType()}.
     * <p>
     * The results of each visit are cached. If an element has already been
     * visited before the cached result is used.
     * <p>
     * If the specified element is a expression and the value of the expression
     * can already be determined without solving, the expression is treated as
     * a constant value.
     *
     * @param elem the specified element
     * @return the result of the selected visit method
     * @throws UnknownTypeException if the element type is not known by this visitor
     */
    public T visit(Elem elem) throws UnknownTypeException {
        if (visited.containsKey(elem)) {
            return visited.get(elem);
        }
        T result;
        if (elem.getType() != Type.ConstantBoolExpr &&
                (elem instanceof BoolExpr) &&
                ((BoolExpr) elem).hasValue() &&
                !(elem instanceof Var)) {
            result = visit(BoolExpr.constant(((BoolExpr) elem).getValue()));
        } else if (elem.getType() != Type.ConstantIntExpr &&
                elem instanceof IntExpr &&
                ((IntExpr) elem).hasValue() &&
                !(elem instanceof Var)) {
            result = visit(IntExpr.constant(((IntExpr) elem).getValue()));
        } else {
            result = typeVisit(elem);
        }
        visited.put(elem, result);
        return result;
    }

    protected T typeVisit(Elem elem) {
        switch (elem.getType()) {
            case Elem:
                return visitElem(elem);
            // Constraints
            case Constraint:
                return visitConstraint((Constraint) elem);
            case BoolExprConstraint:
                return visitBoolExprConstraint((BoolExprConstraint) elem);
            // Expressions
            case Expr:
                return visitExpr((Expr) elem);
            case UnExpr:
                return visitUnExpr((UnExpr) elem);
            case BiExpr:
                return visitBiExpr((BiExpr) elem);
            // Bool expressions
            case BoolExpr:
                return visitBoolExpr((BoolExpr) elem);
            // Unary bool expressions
            case UnBoolExpr:
                return visitUnBoolExpr((UnBoolExpr) elem);
            case NotExpr:
                return visitNotExpr((NotExpr) elem);
            case ConstantBoolExpr:
                return visitConstantBoolExpr((ConstantBoolExpr) elem);
            // Binary bool expressions
            case BiBoolExpr:
                return visitBiBoolExpr((BiBoolExpr) elem);
            case AndExpr:
                return visitAndExpr((AndExpr) elem);
            case OrExpr:
                return visitOrExpr((OrExpr) elem);
            // Relational bool expressions
            case ReBoolExpr:
                return visitReBoolExpr((ReBoolExpr) elem);
            case EqExpr:
                return visitEqExpr((EqExpr) elem);
            case NeExpr:
                return visitNeExpr((NeExpr) elem);
            case GtExpr:
                return visitGtExpr((GtExpr) elem);
            case GeExpr:
                return visitGeExpr((GeExpr) elem);
            case LtExpr:
                return visitLtExpr((LtExpr) elem);
            case LeExpr:
                return visitLeExpr((LeExpr) elem);
            // Int expressions
            case IntExpr:
                return visitIntExpr((IntExpr) elem);
            case ConstantIntExpr:
                return visitConstantIntExpr((ConstantIntExpr) elem);
            // Binary int expressions
            case BiIntExpr:
                return visitBiIntExpr((BiIntExpr) elem);
            case AddExpr:
                return visitAddExpr((AddExpr) elem);
            case SubExpr:
                return visitSubExpr((SubExpr) elem);
            case MulExpr:
                return visitMulExpr((MulExpr) elem);
            case DivExpr:
                return visitDivExpr((DivExpr) elem);
            case MinExpr:
                return visitMinExpr((MinExpr) elem);
            case MaxExpr:
                return visitMaxExpr((MaxExpr) elem);
            // Variables
            case Var:
                return visitVar((Var) elem);
            // Bool variables
            case BoolVar:
                return visitBoolVar((BoolVar) elem);
            case ConstantBoolVar:
                return visitConstantBoolVar((ConstantBoolVar) elem);
            case ExpressionBoolVar:
                return visitExpressionBoolVar((ExpressionBoolVar) elem);
            case UpdatableBoolVar:
                return visitUpdatableBoolVar((UpdatableBoolVar) elem);
            case InstantiatableBoolVar:
                return visitInstantiatableBoolVar((InstantiatableBoolVar) elem);
            // Int variables
            case IntVar:
                return visitIntVar((IntVar) elem);
            case ConstantIntVar:
                return visitConstantIntVar((ConstantIntVar) elem);
            case ExpressionIntVar:
                return visitExpressionIntVar((ExpressionIntVar) elem);
            case UpdatableIntVar:
                return visitUpdatableIntVar((UpdatableIntVar) elem);
            case BoundedIntVar:
                return visitBoundedIntVar((BoundedIntVar) elem);
            default:
                throw new UnknownTypeException("Unknown type: %s", elem.getType());
        }
    }
}
