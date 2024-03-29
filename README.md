<p align="center">
  <img width="25%" src="https://github.com/svenkonings/JaCoMo/raw/master/img/JaCoMo.svg?sanitize=true" alt="JaCoMo"/>
</p>

# JaCoMo
JaCoMo is a high-level, solver-independent, Java constraint model for constraint
satisfaction problems in the integer domain.
JaCoMo can be used to model constrain satisfaction problems and then solve them
using different solver implementations.
Currently, supported solvers are:
- [Choco-solver](https://github.com/chocoteam/choco-solver)
- [OR-Tools](https://github.com/google/or-tools)

[![Build](https://github.com/svenkonings/JaCoMo/workflows/build/badge.svg?branch=master&event=push)](https://github.com/svenkonings/JaCoMo/actions?query=workflow%3Abuild+branch%3Amaster+event%3Apush)
[![Maven Central](https://img.shields.io/maven-central/v/nl.svenkonings.jacomo/jacomo.svg?label=Maven%20Central&color=%234c1)](https://search.maven.org/search?q=g:%22nl.svenkonings.jacomo%22)
[![Javadoc](https://javadoc.io/badge2/nl.svenkonings.jacomo/jacomo/javadoc.svg)](https://javadoc.io/doc/nl.svenkonings.jacomo)

## 1. Getting started

### 1.1 Creating a model
The model stores the variables and constraints to solve.
```java
Model model = new Model();
```

### 1.2 Declaring variables
The recommended method to declare variables is by using the model as demonstrated below. These variables are automatically added to the model itself when they are declared.

It is also possible to create variables using the factory methods from the `BoolVar` or `Intvar` interfaces or the class constructors. Those variables have to be added to the model manually using the `addVar` method.

#### 1.2.1 Boolean variables
```java
BoolVar b1 = model.boolVar();               // Boolean variable with generated name (_bool_0, _bool_1, etc.)
BoolVar b2 = model.boolVar("x");            // Boolean variable with given name
BoolVar b3 = model.boolVar(true);           // Boolean variable with generated name and constant value
BoolVar b4 = model.boolVar("y", true);      // Boolean variable with given name and constant value
BoolVar b5 = model.boolVar(b1.and(b2));     // Boolean variable with generated name and given expression
BoolVar b6 = model.boolVar("z", b3.or(b4)); // Boolean variable with given name and given expression
```
#### 1.2.2 Integer variables
```java
IntVar i1  = model.intVar();                // Integer variable with generated name (_int_0, _int_1, etc.)
IntVar i2  = model.intVar("u");             // Integer variable with given name
IntVar i3  = model.intVar(3);               // Integer variable with generated name and given value
IntVar i4  = model.intVar("v", 4);          // Integer variable with given name and given value
IntVar i5  = model.intVar(i1.add(i2));      // Integer variable with generated name and given expression
IntVar i6  = model.intVar("w", i3.mul(i4)); // Integer variable with given name and given expression
IntVar i7  = model.intVar(1, 7);            // Integer variable with generated name and given bounds
IntVar i8  = model.intVar("x", 1, 8);       // Integer variable with given name and given bounds
IntVar i9  = model.intVarLb(9);             // Integer variable with generated name and given lower bound
IntVar i10 = model.intVarLb("y", 10);       // Integer variable with given name and given lower bound
IntVar i11 = model.intVarUb(11);            // Integer variable with generated name and given upper bound
IntVar i12 = model.intVarUb("z", 12);       // Integer variable with given name and given upper bound
```

### 1.3 Using expressions
Expressions can be used to define variables or to create constraints.

#### 1.3.1 Boolean expressions
```java
BoolExpr b7  = BoolExpr.constant(true); // Create constant
BoolExpr b8  = b1.not();                // Not expression: !b1
BoolExpr b9  = b2.and(b8);              // And expression: b2 && b8
BoolExpr b10 = b8.or(b9);               // Or expression:  b8 || b9
```
There are also vararg methods available to chain expressions:
```java
BoolExpr b11 = BoolExpr.and(b7, b8, b9);     // And expression: b7 && b8 && b9
BoolExpr b12 = BoolExpr.or(b7, b8, b9, b10); // Or expression:  b7 || b8 || b9 || b10
```

#### 1.3.2 Integer expressions
```java
IntExpr i13 = IntExpr.constant(1); // Create constant
IntExpr i14 = i10.add(i13);        // Add expr: i10 + i13
IntExpr i15 = i13.sub(i14);        // Sub expr: i13 - i14
IntExpr i16 = i14.mul(i15);        // Mul expr: i14 * i15
IntExpr i17 = i13.div(i15);        // Div expr: i13 / i15
IntExpr i18 = i16.max(i17);        // Max expr: max(i16, i17)
IntExpr i19 = i16.min(i17);        // Min expr: min(i16, i17)
```
There are also vararg methods available to chain expressions:
```java
IntExpr i20 = IntExpr.add(i13, i14, i15, i16);      // Add expr: i13 + i14 + i15 + i16
IntExpr i21 = IntExpr.sub(i14, i15, i16, i17);      // Sub expr: ((i14 - i15) - i16) - i17
IntExpr i22 = IntExpr.mul(i13, i14, i15);           // Mul expr: i13 * i14 * i15
IntExpr i23 = IntExpr.div(i17, i18, i19);           // Div expr: (i17 / i18) / i19
IntExpr i24 = IntExpr.max(i15, i16, i17, i18, i19); // Max expr: max(i15, i16, i17, i18, i19)
IntExpr i25 = IntExpr.min(i14, i15, i16, i17, i18); // Min expr: min(i14, i15, i16, i17, i18)
```

#### 1.3.3 Relational expressions
Relational expressions are expressions that compare two integers and result in a boolean expression.
```java
BoolExpr b13 = i10.eq(i20); // Eq expr: i10 == i20
BoolExpr b14 = i11.ne(i21); // Ne expr: i11 != i21
BoolExpr b15 = i22.gt(i23); // Gt expr: i22 >  i23
BoolExpr b16 = i15.ge(i21); // Ge expr: i15 >= i21
BoolExpr b17 = i17.le(i19); // Le expr: i17 <= i19
BoolExpr b18 = i18.lt(i22); // Lt expr: i18 <  i22
```

### 1.4 Adding constraints
#### 1.4.1 Boolean expression constraints
Boolean expression constraints receive a boolean expression that should always hold.
```java
model.constraint(i1.gt(i2)); // i1 should always be greater than i2
model.constraint(b1.or(b2)); // b1 should hold or b2 should hold
```

### 1.5 Solving a model
Models can be solved using a Solver. The Solver returns a map of variables and their solved values. Solvers can also update the variables of the model directly.
```java
Model model = new Model();
IntVar x = model.intVarLb("x", 2); // x > 2
Intvar y = model.intVarUb("y", 10); // y < 10
model.constraint(x.add(IntExpr.constant(2).mul(y)).eq(IntExpr.constant(7))); // x + 2 * y == 7

Solver solver = new ChocoSolver() // Pick solver implementation
VarMap vars = solver.solve(model); // Solves the model and returns variable map with resulting values
IntVar x2 = vars.getVar("x"); // Get the solved variable x
x2.getValue() // 3, the solved variable x has value 3
x.getValue(); // null, the model has not been updated thus x has not been instantiated
vars.getVar("y").getValue() // 2, the solved variable y has value 2

boolean solved = solver.solveAndUpdate(model) // Solves the model and updates the variables directly
x.getValue() // 3, the variable x has been updated
```
When there is no solution the solver behaves as follows:
```java
Model model = new Model();
BoolVar z = model.boolVar("z");
model.constraint(z);
model.constraint(z.not());
Solver solver = new ChocoSolver()
VarMap vars = solver.solve(model); // Returns null since there is no viable solution
boolean solved = solver.solveAndUpdate(model) // Solved is false since there is no viable solution,
                                              // the model has not been updated

```

## 2. Extending functionality
### 2.1 Element overview
See the image below for an overview of available elements and how they relate to each other. A solid arrow indicates the parent element. Dashed arrows indicate additional implemented interfaces.

[![Elements](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg?sanitize=true)](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg)
<sup>*Click image for larger version*</sup>

### 2.2 Custom visitors
Custom visitors can be added by implementing the `Visitor` interface. When a visitor encounters an element, it will try to call its visit implementation for that element type. If no immediate implementation is found, the visitor will traverse up the parent tree till it finds an implementation for the visited element. If no implementation is found, a `NotImplementedException` is thrown.

### 2.3 Custom elements
Custom elements should extend the `Elem` interface. When adding custom elements the `Visitor` interface should also be extended with corresponding visit methods for the new element types and the main `visit` method should be adapted as well (only for the new element types, for existing elements `super.visit` can be used).

Existing `Visitor` implementations also need to be extended to use them with the new element types. The extended visitor only needs to implement visit methods for the new element types. Existing elements will be handled by the base visitor implementation (although the implementations can be overwritten if desired).

## 3. Progress
Version 0.1 is a minimal viable product. It contains the following:

- Basic model containing variables and constraints
  - Basic variables
    - Boolean
    - Integer
  - Basic expressions
    - Constants
      - Boolean
      - Integer
    - Boolean logic
      - And
      - Or
      - Not
    - Relational expressions
      - Equals
      - Not equals
      - Lesser than
      - Lesser or equals
      - Greater than
      - Greater or equals
    - Basic arithmetic expressions
      - Addition
      - Subtraction
      - Multiplication
      - Integer division
      - Minimum
      - Maximum
  - Boolean constraints
- Model factory methods for creating variables
- Visitor pattern for traversing the model
- Solver implementations
  - Choco-solver
  - OR-tools
- Unit-test for non-trivial classes and system tests
- Getting started documentation with examples
- JavaDoc documentation
- Maven Central Repository release

Future goals are to:

- Expand the model with more operations
  - Xor expression
  - Real variables
  - Additional types of constraints
- Add more solver implementations
  - MiniZinc
  - OscaR
  - JaCop
  - Yuck
