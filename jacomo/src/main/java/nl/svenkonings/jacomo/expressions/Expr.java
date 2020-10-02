package nl.svenkonings.jacomo.expressions;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import org.jetbrains.annotations.NotNull;

public interface Expr extends Elem {
    @Override
    default @NotNull Type getType() {
        return Type.Expr;
    }
}