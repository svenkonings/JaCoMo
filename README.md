<p align="center">
  <img width="25%" src="https://github.com/svenkonings/JaCoMo/raw/master/img/JaCoMo.svg?sanitize=true" alt="JaCoMo"/>
</p>

# JaCoMo
JaCoMo is a high-level, solver-independent, Java constraint model for constraint
satisfaction problems in the integer domain.

[![Build](https://github.com/svenkonings/JaCoMo/workflows/build/badge.svg?branch=master&event=push)](https://github.com/svenkonings/JaCoMo/actions?query=workflow%3Abuild+branch%3Amaster+event%3Apush)
[![Maven Central](https://img.shields.io/maven-central/v/nl.svenkonings.jacomo/jacomo.svg?label=Maven%20Central&color=%234c1)](https://search.maven.org/search?q=g:%22nl.svenkonings.jacomo%22)
[![Javadoc](https://javadoc.io/badge2/nl.svenkonings.jacomo/jacomo/javadoc.svg)](https://javadoc.io/doc/nl.svenkonings.jacomo)

## Element overview
[![Elements](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg?sanitize=true)](https://github.com/svenkonings/JaCoMo/raw/master/img/Elements.svg)

## Progress
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
