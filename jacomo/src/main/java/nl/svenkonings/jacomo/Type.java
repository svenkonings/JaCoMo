package nl.svenkonings.jacomo;

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
