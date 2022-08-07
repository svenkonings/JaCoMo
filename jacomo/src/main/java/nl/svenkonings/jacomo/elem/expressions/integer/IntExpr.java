/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.integer;

import nl.svenkonings.jacomo.elem.expressions.Expr;
import nl.svenkonings.jacomo.elem.expressions.bool.relational.*;
import nl.svenkonings.jacomo.elem.expressions.integer.binary.*;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
import org.jetbrains.annotations.Nullable;

import static nl.svenkonings.jacomo.util.ArrayUtil.foldLeft;

/**
 * Represents an integer expression.
 */
public interface IntExpr extends Expr {
    @Override
    @Nullable Integer getValue();

    /**
     * Returns whether this integer expression has a (instantiated) lower-bound.
     *
     * @return {@code true} if this expression has a lower-bound
     */
    boolean hasLowerBound();

    /**
     * Returns the lower-bound of this expression
     *
     * @return the lower-bound of this expression, or {@code null} if it is uninstantiated
     */
    @Nullable Integer getLowerBound();

    /**
     * Returns whether this integer expression has a (instantiated) upper-bound.
     *
     * @return {@code true} if this expression has an upper-bound
     */
    boolean hasUpperBound();

    /**
     * Returns the upper-bound of this expression
     *
     * @return the upper-bound of this expression, or {@code null} if it is uninstantiated
     */
    @Nullable Integer getUpperBound();

    // Factory methods

    /**
     * Creates a new integer constant with the specified value.
     *
     * @param value the specified value
     * @return the created integer constant
     */
    static ConstantIntExpr constant(int value) {
        return new ConstantIntExpr(value);
    }

    // Binary int expressions

    /**
     * Creates a new integer expression adding the specified expression to this expression.
     *
     * @param other the specified expression
     * @return the created Addition expression
     */
    default AddExpr add(IntExpr other) {
        return new AddExpr(this, other);
    }

    /**
     * Creates a new integer expression adding the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Addition expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static AddExpr add(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, AddExpr::new);
    }

    /**
     * Creates a new integer expression subtracting the specified expression from this expression.
     *
     * @param other the specified expression
     * @return the created Subtraction expression
     */
    default SubExpr sub(IntExpr other) {
        return new SubExpr(this, other);
    }

    /**
     * Creates a new integer expression subtracting the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Subtraction expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static SubExpr sub(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, SubExpr::new);
    }

    /**
     * Creates a new integer expression multiplying this expression with the specified expression.
     *
     * @param other the specified expression
     * @return the created Multiplication expression
     */
    default MulExpr mul(IntExpr other) {
        return new MulExpr(this, other);
    }

    /**
     * Creates a new integer expression multiplying the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Multiplication expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static MulExpr mul(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, MulExpr::new);
    }

    /**
     * Creates a new integer expression dividing this expression by the specified expression.
     *
     * @param other the specified expression
     * @return the created Division expression
     */
    default DivExpr div(IntExpr other) {
        return new DivExpr(this, other);
    }

    /**
     * Creates a new integer expression dividing the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Division expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static DivExpr div(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, DivExpr::new);
    }

    /**
     * Creates a new integer expression representing the minimum of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Minimum expression
     */
    default MinExpr min(IntExpr other) {
        return new MinExpr(this, other);
    }

    /**
     * Creates a new integer expression taking the minimum of the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Minimum expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static MinExpr min(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, MinExpr::new);
    }

    /**
     * Creates a new integer expression representing the maximum of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Maximum expression
     */
    default MaxExpr max(IntExpr other) {
        return new MaxExpr(this, other);
    }

    /**
     * Creates a new integer expression taking the maximum the specified elements from left to right.
     *
     * @param exprs the specified elements
     * @return the created Maximum expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static MaxExpr max(IntExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, MaxExpr::new);
    }

    // Relational bool expressions

    /**
     * Creates a new Equals expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Equals expression
     */
    default EqExpr eq(IntExpr other) {
        return new EqExpr(this, other);
    }

    /**
     * Creates a new Not-equals expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Not-equals expression
     */
    default NeExpr ne(IntExpr other) {
        return new NeExpr(this, other);
    }

    /**
     * Creates a new Lesser-than expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Lesser-than expression
     */
    default LtExpr lt(IntExpr other) {
        return new LtExpr(this, other);
    }

    /**
     * Creates a new Lesser-or-equals expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Lesser-or-equals expression
     */
    default LeExpr le(IntExpr other) {
        return new LeExpr(this, other);
    }

    /**
     * Creates a new Greater-than expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Greater-than expression
     */
    default GtExpr gt(IntExpr other) {
        return new GtExpr(this, other);
    }

    /**
     * Creates a new Greater-or-equals expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Greater-or-equals expression
     */
    default GeExpr ge(IntExpr other) {
        return new GeExpr(this, other);
    }
}
