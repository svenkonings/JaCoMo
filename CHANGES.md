# JaCoMo changelog
This changelog gives a high-level overview of the (planned) changes per version.

## Future versions
After the MVP is done the goals are to:

- Expand the model with more operations
  - Xor expression
  - Real variables
  - Additional types of constraints
- Add more solver implementations
  - MiniZinc
  - OscaR
  - JaCop
  - Yuck

## v0.1 - MVP (In progress)
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
  - ChocoSolver
  - OR-tools
- Unit-test for non-trivial classes and system tests
- Getting started documentation with examples
- JavaDoc documentation
- Maven Central Repository release

## v0.1-RC3
The third release candidate of version 0.1. The following has changed from RC2:

- Update OR-Tools to avoid crash when solving multiple times in the same process ([github.com/google/or-tools/issues/2091](https://github.com/google/or-tools/issues/2091))
- Add methods to solve Models without updating existing elements, rename existing solve methods adequately
- Add factory methods for creating variables
- Add factory methods for creating nested binary expressions
- Make model copyable
- Add methods for accessing model elements as Stream
- Also optimize subexpressions of expression variables in checker
- Add optimization for divisions with upper bound of zero
- Filter duplicate elements in checker
- Move all elements inside `elem` package
- Add methods to list variable names in `VarMap` and `Model`
- Rename `Model.visitAll` to `Model.visit`
- Add unit tests for boolean expressions and binary integer expressions
- Fix typo in `GeExpr.getValue`
- Include element names in hashcode generation to avoid hash collisions with similarly structured trees
- Update dependencies

## v0.1-RC2
The second release candidate of version 0.1. The following has changed from RC1:

- Fix bounded intVar hasValue returning true when both bounds are null
- Fix ChocoSolver expression to constraint conversion for certain expressions
- Solve models by updating unresolved variables directly instead of returning a
  map with new variables
- Add Checker containing model checks and optimizations
- Rework visitor caching to prevent posting reified constraints
- Enable Continuous Integration

## v0.1-RC1
The first release candidate of version 0.1. It contains the following:

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
  - ChocoSolver
  - OR-tools
- System tests
- JavaDoc documentation
- Maven Central Repository release

