package nl.svenkonings.jacomo.elem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a basic element with a tree-like structure.
 */
public interface Elem {
    /**
     * Get the children elements of this element.
     * The list cannot be modified.
     *
     * @return the list of children.
     */
    @NotNull List<? extends Elem> getChildren();

    /**
     * Get the type of this element.
     *
     * @return the type.
     */
    default @NotNull Type getType() {
        return Type.Elem;
    }
}
