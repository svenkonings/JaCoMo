package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.exceptions.ContradictionException;

public interface UpdatableBoolVar extends BoolVar {
    void updateValue(boolean value) throws ContradictionException;
}
