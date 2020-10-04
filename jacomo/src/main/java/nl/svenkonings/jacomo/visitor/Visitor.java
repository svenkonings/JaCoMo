package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.unchecked.NotImplementedException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import nl.svenkonings.jacomo.expressions.Expr;
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
     * to use based on the result of {@link Elem#getType()}. The results of each
     * visit are cached. If an element has already been visited before the cached
     * result is used.
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
        switch (elem.getType()) {
            case Elem:
                result = visitElem(elem);
                break;
            // Constraints
            case Constraint:
                result = visitConstraint((Constraint) elem);
                break;
            case BoolExprConstraint:
                result = visitBoolExprConstraint((BoolExprConstraint) elem);
                break;
            // Expressions
            case Expr:
                result = visitExpr((Expr) elem);
                break;
            // Bool expressions
            case BoolExpr:
                result = visitBoolExpr((BoolExpr) elem);
                break;
            // Unary bool expressions
            case UnBoolExpr:
                result = visitUnBoolExpr((UnBoolExpr) elem);
                break;
            case NotExpr:
                result = visitNotExpr((NotExpr) elem);
                break;
            case ConstantBoolExpr:
                result = visitConstantBoolExpr((ConstantBoolExpr) elem);
                break;
            // Binary bool expressions
            case BiBoolExpr:
                result = visitBiBoolExpr((BiBoolExpr) elem);
                break;
            case AndExpr:
                result = visitAndExpr((AndExpr) elem);
                break;
            case OrExpr:
                result = visitOrExpr((OrExpr) elem);
                break;
            // Relational bool expressions
            case ReBoolExpr:
                result = visitReBoolExpr((ReBoolExpr) elem);
                break;
            case EqExpr:
                result = visitEqExpr((EqExpr) elem);
                break;
            case NeExpr:
                result = visitNeExpr((NeExpr) elem);
                break;
            case GtExpr:
                result = visitGtExpr((GtExpr) elem);
                break;
            case GeExpr:
                result = visitGeExpr((GeExpr) elem);
                break;
            case LtExpr:
                result = visitLtExpr((LtExpr) elem);
                break;
            case LeExpr:
                result = visitLeExpr((LeExpr) elem);
                break;
            // Int expressions
            case IntExpr:
                result = visitIntExpr((IntExpr) elem);
                break;
            case ConstantIntExpr:
                result = visitConstantIntExpr((ConstantIntExpr) elem);
                break;
            // Binary int expressions
            case BiIntExpr:
                result = visitBiIntExpr((BiIntExpr) elem);
                break;
            case AddExpr:
                result = visitAddExpr((AddExpr) elem);
                break;
            case SubExpr:
                result = visitSubExpr((SubExpr) elem);
                break;
            case MulExpr:
                result = visitMulExpr((MulExpr) elem);
                break;
            case DivExpr:
                result = visitDivExpr((DivExpr) elem);
                break;
            case MinExpr:
                result = visitMinExpr((MinExpr) elem);
                break;
            case MaxExpr:
                result = visitMaxExpr((MaxExpr) elem);
                break;
            // Variables
            case Var:
                result = visitVar((Var) elem);
                break;
            // Bool variables
            case BoolVar:
                result = visitBoolVar((BoolVar) elem);
                break;
            case ConstantBoolVar:
                result = visitConstantBoolVar((ConstantBoolVar) elem);
                break;
            case ExpressionBoolVar:
                result = visitExpressionBoolVar((ExpressionBoolVar) elem);
                break;
            case UpdatableBoolVar:
                result = visitUpdatableBoolVar((UpdatableBoolVar) elem);
                break;
            case InstantiatableBoolVar:
                result = visitInstantiatableBoolVar((InstantiatableBoolVar) elem);
                break;
            // Int variables
            case IntVar:
                result = visitIntVar((IntVar) elem);
                break;
            case ConstantIntVar:
                result = visitConstantIntVar((ConstantIntVar) elem);
                break;
            case ExpressionIntVar:
                result = visitExpressionIntVar((ExpressionIntVar) elem);
                break;
            case UpdatableIntVar:
                result = visitUpdatableIntVar((UpdatableIntVar) elem);
                break;
            case BoundedIntVar:
                result = visitBoundedIntVar((BoundedIntVar) elem);
                break;
            default:
                throw new UnknownTypeException("Unknown type: %s", elem.getType());
        }
        visited.put(elem, result);
        return result;
    }
}
