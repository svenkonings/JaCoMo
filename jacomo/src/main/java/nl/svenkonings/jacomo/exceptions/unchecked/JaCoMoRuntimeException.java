/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Parent exception of all unchecked JaCoMo exceptions.
 */
public class JaCoMoRuntimeException extends RuntimeException {
    protected JaCoMoRuntimeException(String message) {
        super(message);
    }

    protected JaCoMoRuntimeException(String message, Object... args) {
        this(String.format(message, args));
    }

    protected JaCoMoRuntimeException(Throwable cause, String message) {
        super(message, cause);
    }

    protected JaCoMoRuntimeException(Throwable cause, String message, Object... args) {
        this(String.format(message, args), cause);
    }
}
