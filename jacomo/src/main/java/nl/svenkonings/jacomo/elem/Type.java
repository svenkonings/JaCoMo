/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem;

/**
 * Enumeration of element types.
 */
public enum Type {
    Elem,
    // Constraints
    Constraint,
    BoolExprConstraint,
    // Expressions
    Expr,
    UnExpr,
    BiExpr,
    // Bool expressions
    BoolExpr,
    ConstantBoolExpr,
    // Unary bool expressions
    UnBoolExpr,
    NotExpr,
    // Binary bool expressions
    BiBoolExpr,
    AndExpr,
    OrExpr,
    // Relational bool expressions
    ReBoolExpr,
    EqExpr,
    NeExpr,
    GtExpr,
    GeExpr,
    LtExpr,
    LeExpr,
    // Int expressions
    IntExpr,
    ConstantIntExpr,
    // Binary int expressions
    BiIntExpr,
    AddExpr,
    SubExpr,
    MulExpr,
    DivExpr,
    MinExpr,
    MaxExpr,
    // Variables
    Var,
    // Bool variables
    BoolVar,
    ConstantBoolVar,
    ExpressionBoolVar,
    UpdatableBoolVar,
    InstantiatableBoolVar,
    // Int variables
    IntVar,
    ConstantIntVar,
    ExpressionIntVar,
    UpdatableIntVar,
    BoundedIntVar
}
