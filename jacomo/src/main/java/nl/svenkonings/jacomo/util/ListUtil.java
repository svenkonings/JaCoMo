/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Contains utility functions for lists.
 * Used as replacement for Java 11 List interface static methods.
 */
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

    /**
     * Returns an unmodifiable list view of the specified collection.
     *
     * @param coll the specified collection
     * @param <E>  the element type
     * @return the unmodifiable list
     */
    public static <E> @NotNull List<E> copyOf(@NotNull Collection<? extends E> coll) {
        return Collections.unmodifiableList(new ArrayList<>(coll));
    }
}
