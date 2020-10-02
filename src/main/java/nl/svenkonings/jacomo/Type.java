package nl.svenkonings.jacomo;

public enum Type {
    Elem,
    // Expressions
    Expr,
    // Bool expressions
    BoolExpr,
    ConstantBoolExpr,
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
