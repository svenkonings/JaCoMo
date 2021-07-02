/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

import nl.svenkonings.jacomo.visitor.Visitor;

/**
 * Unchecked exception thrown when a {@link Visitor} method
 * for the visited type or one of its parents is not implemented.
 */
public class NotImplementedException extends JaCoMoRuntimeException {
    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Object... args) {
        super(message, args);
    }

    public NotImplementedException(Throwable cause, String message) {
        super(cause, message);
    }

    public NotImplementedException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
