package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.UnknownTypeException;
import nl.svenkonings.jacomo.expressions.Expr;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.bool.ConstantBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.BiBoolExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.expressions.bool.relational.*;
import nl.svenkonings.jacomo.expressions.integer.ConstantIntExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.expressions.integer.binary.*;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.*;
import nl.svenkonings.jacomo.variables.integer.*;

public interface Visitor<T> {
    // Elements
    default T visitElem(Elem elem) {
        T result = null;
        for (Elem child : elem.getChildren()) {
            result = visit(child);
        }
        return result;
    }

    // Constraints
    default T visitConstraint(Constraint constraint) {
        return visitElem(constraint);
    }

    default T visitBoolExprConstraint(BoolExprConstraint boolExprConstraint) {
        return visitConstraint(boolExprConstraint);
    }

    // Expressions
    default T visitExpr(Expr expr) {
        return visitElem(expr);
    }

    // Bool expressions
    default T visitBoolExpr(BoolExpr boolExpr) {
        return visitExpr(boolExpr);
    }

    default T visitConstantBoolExpr(ConstantBoolExpr constantBoolExpr) {
        return visitBoolExpr(constantBoolExpr);
    }

    // Binary bool expressions
    default T visitBiBoolExpr(BiBoolExpr biBoolExpr) {
        return visitBoolExpr(biBoolExpr);
    }

    default T visitAndExpr(AndExpr andExpr) {
        return visitBiBoolExpr(andExpr);
    }

    default T visitOrExpr(OrExpr orExpr) {
        return visitBiBoolExpr(orExpr);
    }

    // Relational bool expressions
    default T visitReBoolExpr(ReBoolExpr reBoolExpr) {
        return visitBoolExpr(reBoolExpr);
    }

    default T visitEqExpr(EqExpr eqExpr) {
        return visitReBoolExpr(eqExpr);
    }

    default T visitNeExpr(NeExpr neExpr) {
        return visitReBoolExpr(neExpr);
    }

    default T visitGtExpr(GtExpr gtExpr) {
        return visitReBoolExpr(gtExpr);
    }

    default T visitGeExpr(GeExpr geExpr) {
        return visitReBoolExpr(geExpr);
    }

    default T visitLtExpr(LtExpr ltExpr) {
        return visitReBoolExpr(ltExpr);
    }

    default T visitLeExpr(LeExpr leExpr) {
        return visitReBoolExpr(leExpr);
    }

    // Int expressions
    default T visitIntExpr(IntExpr intExpr) {
        return visitExpr(intExpr);
    }

    default T visitConstantIntExpr(ConstantIntExpr constantIntExpr) {
        return visitIntExpr(constantIntExpr);
    }

    // Binary int expressions
    default T visitBiIntExpr(BiIntExpr biIntExpr) {
        return visitIntExpr(biIntExpr);
    }

    default T visitAddExpr(AddExpr addExpr) {
        return visitBiIntExpr(addExpr);
    }

    default T visitSubExpr(SubExpr subExpr) {
        return visitBiIntExpr(subExpr);
    }

    default T visitMulExpr(MulExpr mulExpr) {
        return visitBiIntExpr(mulExpr);
    }

    default T visitDivExpr(DivExpr divExpr) {
        return visitBiIntExpr(divExpr);
    }

    default T visitMinExpr(MinExpr minExpr) {
        return visitBiIntExpr(minExpr);
    }

    default T visitMaxExpr(MaxExpr maxExpr) {
        return visitBiIntExpr(maxExpr);
    }

    // Variables
    default T visitVar(Var var) {
        return visitElem(var);
    }

    // Bool variables
    default T visitBoolVar(BoolVar boolVar) {
        return visitVar(boolVar);
    }

    default T visitConstantBoolVar(ConstantBoolVar constantBoolVar) {
        return visitBoolVar(constantBoolVar);
    }

    default T visitExpressionBoolVar(ExpressionBoolVar expressionBoolVar) {
        return visitBoolVar(expressionBoolVar);
    }

    default T visitUpdatableBoolVar(UpdatableBoolVar updatableBoolVar) {
        return visitBoolVar(updatableBoolVar);
    }

    default T visitInstantiatableBoolVar(InstantiatableBoolVar instantiatableBoolVar) {
        return visitUpdatableBoolVar(instantiatableBoolVar);
    }

    // Int variables
    default T visitIntVar(IntVar intVar) {
        return visitVar(intVar);
    }

    default T visitConstantIntVar(ConstantIntVar constantIntVar) {
        return visitIntVar(constantIntVar);
    }

    default T visitExpressionIntVar(ExpressionIntVar expressionIntVar) {
        return visitIntVar(expressionIntVar);
    }

    default T visitUpdatableIntVar(UpdatableIntVar updatableIntVar) {
        return visitIntVar(updatableIntVar);
    }

    default T visitBoundedIntVar(BoundedIntVar boundedIntVar) {
        return visitUpdatableIntVar(boundedIntVar);
    }

    default T visit(Elem elem) {
        switch (elem.getType()) {
            case Elem:
                return visitElem(elem);
            case Constraint:
                return visitConstraint((Constraint) elem);
            case BoolExprConstraint:
                return visitBoolExprConstraint((BoolExprConstraint) elem);
            // Expressions
            case Expr:
                return visitExpr((Expr) elem);
            // Bool expressions
            case BoolExpr:
                return visitBoolExpr((BoolExpr) elem);
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
