/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Unchecked exception thrown when different variables have the same name during the solving process.
 */
public class DuplicateNameException extends JaCoMoRuntimeException {
    public DuplicateNameException(String message) {
        super(message);
    }

    public DuplicateNameException(String message, Object... args) {
        super(message, args);
    }

    public DuplicateNameException(Throwable cause, String message) {
        super(cause, message);
    }

    public DuplicateNameException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
