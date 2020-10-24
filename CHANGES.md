# JaCoMo changelog
This changelog gives a high-level overview of the (planned) changes per version.

## Future versions
After the MVP is done the goals are to:

- Expand the model with more operations
- Factory methods for automatically nesting binary expressions
- Add more solver implementations
  - MiniZinc
- Enable Continuous Integration

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

## v0.1-RC2
The second release candidate of version 0.2. The following has changed from RC1:

- Fix bounded intVar hasValue returning true when both bounds are null
- Fix ChocoSolver expression to constraint conversion for certain expressions
- Solve models by updating unresolved variables directly instead of returning a
  map with new variables
- Add Checker containing model checks and optimizations
- Rework visitor caching to prevent posting reified constraints

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

