package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.Nullable;

public interface BoolVar extends Var, BoolExpr {
    boolean hasExpression();

    @Nullable BoolExpr getExpression();

    default String boolVarString() {
        String name = getName();
        if (hasValue()) {
            return String.format("bool %s = %b", name, getValue());
        } else {
            return String.format("bool %s", name);
        }
    }
}
