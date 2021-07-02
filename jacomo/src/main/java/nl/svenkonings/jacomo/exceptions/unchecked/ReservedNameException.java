/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.exceptions.unchecked;

import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.model.Model;

/**
 * Unchecked exception thrown when a {@link Model} receives a reserved name.
 */
public class ReservedNameException extends JaCoMoRuntimeException {
    public ReservedNameException(Var var) {
        super("Invalid name: %s. Names starting with underscore are reserved for generated names", var.getName());
    }
}
