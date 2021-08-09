/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import com.google.ortools.linearsolver.MPVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPBool {
    private final @Nullable Boolean constant;
    private @Nullable MPVariable variable;

    public MPBool(boolean constant) {
        this.constant = constant;
        this.variable = null;
    }

    public MPBool(@NotNull MPVariable variable) {
        this.constant = null;
        this.variable = variable;
    }

    public boolean isConstant() {
        return constant != null;
    }

    public Boolean getConstant() {
        return constant;
    }

    public boolean isVariable() {
        return constant == null;
    }

    public boolean hasVariable() {
        return variable != null;
    }

    public MPVariable getVariable() {
        return variable;
    }

    public void setVariable(@NotNull MPVariable variable) {
        this.variable = variable;
    }
}
