/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

import nl.svenkonings.jacomo.elem.Elem;

/**
 * Unchecked exception thrown when a visited element has an unexpected type.
 */
public class UnexpectedTypeException extends JaCoMoRuntimeException {
    public UnexpectedTypeException(Elem elem) {
        super("Unexpected return type for element: %s", elem);
    }

    public UnexpectedTypeException(String message) {
        super(message);
    }

    public UnexpectedTypeException(String message, Object... args) {
        super(message, args);
    }

    public UnexpectedTypeException(Throwable cause, String message) {
        super(cause, message);
    }

    public UnexpectedTypeException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
