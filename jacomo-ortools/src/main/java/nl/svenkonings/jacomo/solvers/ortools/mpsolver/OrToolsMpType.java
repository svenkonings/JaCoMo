/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import org.jetbrains.annotations.Nullable;

public class OrToolsMpType {
    private final @Nullable MPBool bool;
    private final @Nullable MPReal real;

    public static OrToolsMpType none() {
        return new OrToolsMpType(null, null);
    }

    public static OrToolsMpType bool(MPBool bool) {
        return new OrToolsMpType(bool, null);
    }

    public static OrToolsMpType real(MPReal real) {
        return new OrToolsMpType(null, real);
    }

    private OrToolsMpType(@Nullable MPBool bool, @Nullable MPReal real) {
        this.bool = bool;
        this.real = real;
    }

    public boolean isBool() {
        return bool != null;
    }

    public MPBool getBool() {
        return bool;
    }

    public boolean isReal() {
        return real != null;
    }

    public MPReal getReal() {
        return real;
    }
}
