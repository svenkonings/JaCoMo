/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.visitor;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.constraints.Constraint;
import nl.svenkonings.jacomo.elem.expressions.BiExpr;
import nl.svenkonings.jacomo.elem.expressions.Expr;
import nl.svenkonings.jacomo.elem.expressions.UnExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.*;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.*;
import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.util.ElemUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Prints string representations of elements with flattened expressions.
 */
public class ElemPrinter implements Visitor<String> {

    /**
     * Returns a string representation of the specified var with flattened expressions.
     *
     * @param var the specified var
     * @return the string representation
     */
    public String printVar(Var var) {
        if (var instanceof ExpressionBoolVar) {
            String expression = removeBrackets(visit(((ExpressionBoolVar) var).getExpression()));
            return String.format("bool %s = %s", var.getName(), expression);
        } else if (var instanceof ExpressionIntVar) {
            String expression = removeBrackets(visit(((ExpressionIntVar) var).getExpression()));
            return String.format("int %s = %s", var.getName(), expression);
        } else {
            return var.toString();
        }
    }

    /**
     * Returns a string representation of the specified constraint with simplified expressions
     *
     * @param constraint the specified constraint
     * @return the string representation
     */
    public String printConstraint(Constraint constraint) {
        if (constraint instanceof BoolExprConstraint) {
            String expression = removeBrackets(visit(((BoolExprConstraint) constraint).getExpr()));
            return String.format("constraint %s", expression);
        } else {
            return constraint.toString();
        }
    }

    @Override
    public String visitElem(Elem elem) {
        return elem.toString();
    }

    @Override
    public String visitVar(Var var) {
        return var.getName();
    }

    @Override
    public String visitExpressionBoolVar(ExpressionBoolVar var) {
        return visit(var.getExpression());
    }

    @Override
    public String visitExpressionIntVar(ExpressionIntVar var) {
        return visit(var.getExpression());
    }

    @Override
    public String visitNotExpr(NotExpr notExpr) {
        return printUnExpr(notExpr, "!");
    }

    @Override
    public String visitAndExpr(AndExpr andExpr) {
        return printAssociativeBiExpr(andExpr, "&&");
    }

    @Override
    public String visitOrExpr(OrExpr orExpr) {
        return printAssociativeBiExpr(orExpr, "||");
    }

    @Override
    public String visitEqExpr(EqExpr eqExpr) {
        return printBiExpr(eqExpr, "==");
    }

    @Override
    public String visitNeExpr(NeExpr neExpr) {
        return printBiExpr(neExpr, "!=");
    }

    @Override
    public String visitGtExpr(GtExpr gtExpr) {
        return printBiExpr(gtExpr, ">");
    }

    @Override
    public String visitGeExpr(GeExpr geExpr) {
        return printBiExpr(geExpr, ">=");
    }

    @Override
    public String visitLtExpr(LtExpr ltExpr) {
        return printBiExpr(ltExpr, "<");
    }

    @Override
    public String visitLeExpr(LeExpr leExpr) {
        return printBiExpr(leExpr, "<=");
    }

    @Override
    public String visitAddExpr(AddExpr addExpr) {
        return printAssociativeBiExpr(addExpr, "+");
    }

    @Override
    public String visitSubExpr(SubExpr subExpr) {
        return printBiExpr(subExpr, "-");
    }

    @Override
    public String visitMulExpr(MulExpr mulExpr) {
        return printAssociativeBiExpr(mulExpr, "*");
    }

    @Override
    public String visitDivExpr(DivExpr divExpr) {
        return printBiExpr(divExpr, "/");
    }

    @Override
    public String visitMinExpr(MinExpr minExpr) {
        return printAssociativeBiExpr("min", minExpr);
    }

    @Override
    public String visitMaxExpr(MaxExpr maxExpr) {
        return printAssociativeBiExpr("max", maxExpr);
    }

    private String printUnExpr(UnExpr expr, String delimiter) {
        return delimiter + visit(expr.getExpr());
    }

    private String printBiExpr(BiExpr expr, String delimiter) {
        return addBrackets(visit(expr.getLeft()) + addSpaces(delimiter) + visit(expr.getRight()));
    }

    private String printAssociativeBiExpr(BiExpr expr, String delimiter) {
        List<Expr> children = ElemUtil.collectAll(expr);
        return addBrackets(children.stream()
                .map(this::visit)
                .collect(Collectors.joining(addSpaces(delimiter))));
    }

    private String printAssociativeBiExpr(String prefix, BiExpr expr) {
        List<Expr> children = ElemUtil.collectAll(expr);
        return prefix + addBrackets(children.stream()
                .map(this::visit)
                .collect(Collectors.joining(", ")));
    }

    private String addSpaces(String string) {
        return " " + string + " ";
    }

    private String addBrackets(String string) {
        return "(" + string + ")";
    }

    private String removeBrackets(String string) {
        if (string.startsWith("(") && string.endsWith(")")) {
            return string.substring(1, string.length() - 1);
        } else {
            return string;
        }
    }
}
