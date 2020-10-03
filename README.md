<p align="center">
  <img width="25%" src="https://github.com/svenkonings/JaCoMo/raw/master/img/JaCoMo.svg?sanitize=true" />
</p>

# JaCoMo
JaCoMo is a high-level, solver-independent, Java constraint model for constraint
satisfaction problems in the integer domain.

## Progress
The current version is a work-in-progress. The progress for the v0.1 minimum
viable product release is as follows:
- [ ] Basic model containing variables and constraints
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
      - [ ] Not
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
- [ ] Model factory methods for creating variables
- [x] Visitor pattern for traversing the model
- [x] Solver implementations
  - [x] ChocoSolver
  - [ ] OR-tools
- [ ] Unit-test for non-trivial classes and system tests
- [ ] Getting started documentation with examples
- [X] JavaDoc documentation
- [ ] Maven Central Repository release
