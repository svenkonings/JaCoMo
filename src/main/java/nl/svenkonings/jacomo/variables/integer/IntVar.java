package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.NotNull;

public interface IntVar extends Var, IntExpr {
    @Override
    default @NotNull Type getType() {
        return Type.IntVar;
    }

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
