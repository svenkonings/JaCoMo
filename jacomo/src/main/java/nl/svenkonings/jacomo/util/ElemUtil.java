/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.util;

import nl.svenkonings.jacomo.elem.expressions.BiExpr;
import nl.svenkonings.jacomo.elem.expressions.Expr;

import java.util.ArrayList;
import java.util.List;

public class ElemUtil {
    /**
     * Collects all children of chained binary expressions with the same type.
     *
     * @param expr the binary expression to start with
     * @return the list of collected children
     */
    public static List<Expr> collectAll(BiExpr expr) {
        List<Expr> vars = new ArrayList<>();
        collectAll(expr, vars);
        return vars;
    }

    private static void collectAll(BiExpr expr, List<Expr> vars) {
        collectAll(expr, expr.getLeft(), vars);
        collectAll(expr, expr.getRight(), vars);
    }

    private static void collectAll(BiExpr expr, Expr child, List<Expr> vars) {
        if (expr.getType().equals(child.getType())) {
            collectAll((BiExpr) child, vars);
        } else {
            vars.add(child);
        }
    }
}
