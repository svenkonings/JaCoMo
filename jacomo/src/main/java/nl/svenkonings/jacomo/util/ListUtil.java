package nl.svenkonings.jacomo.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtil {
    /**
     * Returns an empty unmodifiable list.
     *
     * @param <E> the list type
     * @return the empty unmodifiable list
     */
    public static <E> @NotNull List<E> of() {
        return Collections.emptyList();
    }

    /**
     * Returns an unmodifiable list of the specified element.
     *
     * @param elem the specified element
     * @param <E>  the element type
     * @return the unmodifiable list
     */
    public static <E> @NotNull List<E> of(@NotNull E elem) {
        return Collections.singletonList(elem);
    }

    /**
     * Returns an unmodifiable list of the specified elements.
     *
     * @param elems the specified elements
     * @param <E>   the element type
     * @return the unmodifiable list
     */
    @SafeVarargs
    public static <E> @NotNull List<E> of(@NotNull E... elems) {
        return Collections.unmodifiableList(Arrays.asList(elems));
    }
}
