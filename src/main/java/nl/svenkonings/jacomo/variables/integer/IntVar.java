package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.Nullable;

public interface IntVar extends Var, IntExpr {
    boolean hasExpression();

    @Nullable IntExpr getExpression();

    default String intVarString() {
        String name = getName();
        Integer lowerBound = getLowerBound();
        Integer upperBound = getUpperBound();
        if (hasValue()) {
            return String.format("int %s = %d", name, lowerBound);
        } else if (lowerBound != null && upperBound != null) {
            return String.format("int %s = %d..%d", name, lowerBound, upperBound);
        } else if (lowerBound != null) {
            return String.format("int %s >= %d", name, lowerBound);
        } else if (upperBound != null) {
            return String.format("int %s <= %d", name, upperBound);
        } else {
            return String.format("int %s", name);
        }
    }
}
