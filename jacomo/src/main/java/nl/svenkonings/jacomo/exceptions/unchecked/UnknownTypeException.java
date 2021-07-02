/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Unchecked exception thrown when an unknown type is encountered.
 */
public class UnknownTypeException extends JaCoMoRuntimeException {
    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(String message, Object... args) {
        super(message, args);
    }

    public UnknownTypeException(Throwable cause, String message) {
        super(cause, message);
    }

    public UnknownTypeException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
