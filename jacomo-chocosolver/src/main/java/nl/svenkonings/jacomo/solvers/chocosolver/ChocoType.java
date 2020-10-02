package nl.svenkonings.jacomo.solvers.chocosolver;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChocoType {
    private final @Nullable ArExpression arExpression;
    private final @Nullable ReExpression reExpression;
    private final @Nullable Constraint constraint;

    public static ChocoType none() {
        return new ChocoType(null, null, null);
    }

    public static ChocoType arExpression(@NotNull ArExpression arExpression) {
        return new ChocoType(arExpression, null, null);
    }

    public static ChocoType reExpression(@NotNull ReExpression reExpression) {
        return new ChocoType(null, reExpression, null);
    }

    public static ChocoType constraint(@NotNull Constraint constraint) {
        return new ChocoType(null, null, constraint);
    }

    private ChocoType(@Nullable ArExpression arExpression, @Nullable ReExpression reExpression, @Nullable Constraint constraint) {
        this.arExpression = arExpression;
        this.reExpression = reExpression;
        this.constraint = constraint;
    }

    public boolean isArExpression() {
        return arExpression != null;
    }

    public @Nullable ArExpression getArExpression() {
        return arExpression;
    }

    public boolean isReExpression() {
        return reExpression != null;
    }

    public @Nullable ReExpression getReExpression() {
        return reExpression;
    }

    public boolean isConstraint() {
        return constraint != null;
    }

    public @Nullable Constraint getConstraint() {
        return constraint;
    }
}
