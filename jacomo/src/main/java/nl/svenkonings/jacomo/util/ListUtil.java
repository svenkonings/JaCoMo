package nl.svenkonings.jacomo.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtil {
    public static <E> @NotNull List<E> of() {
        return Collections.emptyList();
    }

    public static <E> @NotNull List<E> of(@NotNull E elem) {
        return Collections.singletonList(elem);
    }

    @SafeVarargs
    public static <E> @NotNull List<E> of(@NotNull E... elems) {
        return Collections.unmodifiableList(Arrays.asList(elems));
    }
}
