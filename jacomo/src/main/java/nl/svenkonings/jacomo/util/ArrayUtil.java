/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.util;

import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;

import java.util.function.BiFunction;

/**
 * Contains utility functions for arrays.
 */
public class ArrayUtil {

    /**
     * Reduces the elements of the specified array into a single result by
     * recursively applying the specified function.
     * <p>
     * The function is first applied to the first two elements. Then the
     * function is applied to the previous result and the next element
     * till there are no more elements left.
     *
     * @param array    the specified array of elements
     * @param function the specified reduce function
     * @param <T>      the type of the elements
     * @param <U>      the result type. Since the first element is used as the initial result,
     *                 the result type has to extend {@link T}
     * @return the final result
     * @throws InvalidInputException if the array has less than two elements
     */
    public static <T, U extends T> U foldLeft(T[] array, BiFunction<T, T, U> function) throws InvalidInputException {
        if (array.length < 1) {
            throw new InvalidInputException("At least 2 elements are required");
        }
        U result = function.apply(array[0], array[1]);
        for (int i = 2; i < array.length; i++) {
            result = function.apply(result, array[i]);
        }
        return result;
    }
}
