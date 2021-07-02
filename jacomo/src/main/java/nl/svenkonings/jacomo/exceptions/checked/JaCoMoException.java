/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.checked;

/**
 * Parent exception of all checked JaCoMo exceptions.
 */
public class JaCoMoException extends Exception {
    protected JaCoMoException(String message) {
        super(message);
    }

    protected JaCoMoException(String message, Object... args) {
        this(String.format(message, args));
    }

    protected JaCoMoException(Throwable cause, String message) {
        super(message, cause);
    }

    protected JaCoMoException(Throwable cause, String message, Object... args) {
        this(String.format(message, args), cause);
    }
}
