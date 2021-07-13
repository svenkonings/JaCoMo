<p align="center">
  <img width="25%" src="https://github.com/svenkonings/JaCoMo/raw/master/img/JaCoMo.svg?sanitize=true" alt="JaCoMo"/>
</p>

# JaCoMo
JaCoMo is a high-level, solver-independent, Java constraint model for constraint
satisfaction problems in the integer domain.

[![Build](https://github.com/svenkonings/JaCoMo/workflows/build/badge.svg?branch=master&event=push)](https://github.com/svenkonings/JaCoMo/actions?query=workflow%3Abuild+branch%3Amaster+event%3Apush)
[![Maven Central](https://img.shields.io/maven-central/v/nl.svenkonings.jacomo/jacomo.svg?label=Maven%20Central&color=%234c1)](https://search.maven.org/search?q=g:%22nl.svenkonings.jacomo%22)
[![Javadoc](https://javadoc.io/badge2/nl.svenkonings.jacomo/jacomo/javadoc.svg)](https://javadoc.io/doc/nl.svenkonings.jacomo)

## 1. Element overview
[![Elements](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg?sanitize=true)](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg)

## 2. Getting started

### 2.1 Creating a model
The model stores the variables and constraints to solve.
```java
Model model = new Model();
```

### 2.2 Declaring variables
The recommended method to declare variables is by using the model as demonstrated below. These variables are automatically added to the model itself when they are declared.

It is also possible to create variables using the factory methods from the `BoolVar` or `Intvar` interfaces or the class constructors. Those variables have to be added to the model manually using the `addVar` method.

#### 2.2.1 Boolean variables
```java
BoolVar b1 = model.boolVar();               // Boolean variable with generated name (_bool_0, _bool_1, etc.)
BoolVar b2 = model.boolVar("x");            // Boolean variable with given name
BoolVar b3 = model.boolVar(true);           // Boolean variable with generated name and constant value
BoolVar b4 = model.boolVar("y", true);      // Boolean variable with given name and constant value
BoolVar b5 = model.boolVar(b1.and(b2));     // Boolean variable with generated name and given expression
BoolVar b6 = model.boolVar("z", b3.or(b4)); // Boolean variable with given name and given expression
```
#### 2.2.2 Integer variables
```java
IntVar i1  = model.intVar();                // Integer variable with generated name
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

### 2.3 Using expressions
Expressions can be used to define variables or to create constraints.

#### 2.3.1 Boolean expressions
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

#### 2.3.2 Integer expressions
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

#### 2.3.3 Relational expressions
Relational expressions are expressions that compare two integers and result in a boolean expression.
```java
BoolExpr b13 = i10.eq(i20); // Eq expr: i10 == i20
BoolExpr b14 = i11.ne(i21); // Ne expr: i11 != i21
BoolExpr b15 = i22.gt(i23); // Gt expr: i22 >  i23
BoolExpr b16 = i15.ge(i21); // Ge expr: i15 >= i21
BoolExpr b17 = i17.le(i19); // Le expr: i17 <= i19
BoolExpr b18 = i18.lt(i22); // Lt expr: i18 <  i22
```

## 2.4 Adding constraints

## 2.5 Solving a model

## 3. Progress
The current version is a work-in-progress. The progress for the v0.1 minimum
viable product release is as follows:
- [x] Basic model containing variables and constraints
  - [x] Basic variables
    - [x] Boolean
    - [x] Integer
  - [x] Basic expressions
    - [x] Constants
      - [x] Boolean
      - [x] Integer
    - [x] Boolean logic
      - [x] And
      - [x] Or
      - [x] Not
    - [x] Relational expressions
      - [x] Equals
      - [x] Not equals
      - [x] Lesser than
      - [x] Lesser or equals
      - [x] Greater than
      - [x] Greater or equals
    - [x] Basic arithmetic expressions
      - [x] Addition
      - [x] Subtraction
      - [x] Multiplication
      - [x] Integer division
      - [x] Minimum
      - [x] Maximum
  - [x] Boolean constraints
- [x] Model factory methods for creating variables
- [x] Visitor pattern for traversing the model
- [x] Solver implementations
  - [x] ChocoSolver
  - [x] OR-tools
- [ ] Unit-test for non-trivial classes and system tests
- [ ] Getting started documentation with examples
- [x] JavaDoc documentation
- [x] Maven Central Repository release
