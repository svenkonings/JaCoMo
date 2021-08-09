/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import com.google.ortools.linearsolver.MPVariable;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MPReal {
    public static class Term {
        private final @NotNull MPVariable variable;
        private final double coefficient;

        public Term(@NotNull MPVariable variable, double coefficient) {
            this.variable = variable;
            this.coefficient = coefficient;
        }

        public @NotNull MPVariable getVariable() {
            return variable;
        }

        public double getCoefficient() {
            return coefficient;
        }

        public Term withCoefficient(double coefficient) {
            return new Term(this.variable, coefficient);
        }
    }

    private final @NotNull List<Term> terms;
    private final double constant;
    private @Nullable MPVariable variable;

    public MPReal() {
        this(0.0);
    }

    public MPReal(double constant) {
        this(Collections.emptyList(), constant);
    }

    public MPReal(MPVariable variable) {
        this(ListUtil.of(new Term(variable, 1.0)));
    }

    public MPReal(List<Term> terms) {
        this(terms, 0.0);
    }

    public MPReal(List<Term> terms, double constant) {
        this.terms = Collections.unmodifiableList(terms);
        this.constant = constant;
        if (isVariable()) {
            this.variable = terms.get(0).variable;
            ;
        }
    }

    public boolean isConstant() {
        return terms.isEmpty();
    }

    public double getConstant() {
        return constant;
    }

    public MPReal withConstant(double constant) {
        return new MPReal(this.terms, constant);
    }

    public boolean isVariable() {
        return constant == 0.0 && terms.size() == 1 && terms.get(0).coefficient == 1.0;
    }

    public boolean hasVariable() {
        return variable != null;
    }

    public MPVariable getVariable() {
        return variable;
    }

    public void setVariable(@NotNull MPVariable variable) {
        if (hasVariable()) {
            throw new InvalidInputException("Real already has a variable");
        }
        this.variable = variable;
    }

    public boolean isVector() {
        return !isConstant() && !isVariable();
    }

    public @NotNull List<Term> getTerms() {
        return terms;
    }

    public MPReal withTerms(@NotNull List<Term> terms) {
        return new MPReal(terms, this.constant);
    }

    public MPReal add(List<Term> addTerms, double constant) {
        List<MPReal.Term> newTerms = new ArrayList<>(terms);
        Map<String, Integer> indexes = new HashMap<>();
        for (int i = 0; i < terms.size(); i++) {
            indexes.put(terms.get(i).getVariable().name(), i);
        }
        for (MPReal.Term term : addTerms) {
            String name = term.getVariable().name();
            if (indexes.containsKey(name)) {
                int index = indexes.get(name);
                MPReal.Term original = newTerms.get(index);
                newTerms.set(index, original.withCoefficient(original.getCoefficient() + term.getCoefficient()));
            } else {
                newTerms.add(term);
            }
        }
        return new MPReal(newTerms, this.constant + constant);
    }
}
