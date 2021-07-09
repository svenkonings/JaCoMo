/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Unchecked exception thrown when an invalid input is given.
 */
public class InvalidInputException extends JaCoMoRuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Object... args) {
        super(message, args);
    }

    public InvalidInputException(Throwable cause, String message) {
        super(cause, message);
    }

    public InvalidInputException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
