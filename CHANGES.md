# JaCoMo changelog
This changelog gives a high-level overview of the (planned) changes per version.

## v0.1 - MVP
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


## Future versions
After the MVP is done the goals are to:

- Expand the model with more operations
- Add more solver implementations
- Enable Continuous Integration
