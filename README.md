# Matrix Operations

A small Java library for performing matrix operations — multiplication, row reduction / rank, determinant, and elementary row operations — over any type that extends `Number`, with dedicated, exact support for rational numbers via a custom `RationalNumber` interface.

## Features

- **Matrix multiplication**
  - Generic version for any `T extends Number` (returns a `Double[][]`)
  - Exact version for `RationalNumber[][]` (returns `RationalNumber[][]`, no floating-point rounding)
- **Determinant** (`matrixDet`)
  - Recursive cofactor expansion, with a generic `Double`-based version and an exact `RationalNumber`-based version
  - Row/column selection is optimized to reduce the number of recursive calls
- **Matrix rank** (`matrixRank`, `matrixRankInPlace`)
  - Full Gaussian elimination over `RationalNumber` matrices, built entirely from the elementary row operations below
- **Elementary row operations** (each with an in-place and a copy-returning variant)
  - `rowInterchange` — swap two rows
  - `rowDivision` — divide a row by a scalar
  - `rowSubtraction` — subtract a scaled row from another row
- **Utility helpers**
  - `minorMatrix` — generic submatrix extraction (used by determinant calculation)
  - `matrixCopy` — deep copy of a `RationalNumber[][]`
- **Two `RationalNumber` implementations**
  - `FractionRationalNumber` — standard reduced fraction (`numerator/denominator`)
  - `MixedRationalNumber` — mixed number representation (`whole and numerator/denominator`)

## Usage

```java
RationalNumber a = new FractionRationalNumber(1, 2);
RationalNumber b = new FractionRationalNumber(1, 3);

RationalNumber sum = a.add(b);        // 5/6
RationalNumber product = a.multiply(b); // 1/6

RationalNumber[][] matA = {
    { new FractionRationalNumber(1, 1), new FractionRationalNumber(2, 1) },
    { new FractionRationalNumber(3, 1), new FractionRationalNumber(4, 1) }
};
RationalNumber[][] matB = {
    { new FractionRationalNumber(5, 1), new FractionRationalNumber(6, 1) },
    { new FractionRationalNumber(7, 1), new FractionRationalNumber(8, 1) }
};

RationalNumber[][] product = MatrixOperations.matrixMultiply(matA, matB);
RationalNumber det = MatrixOperations.matrixDet(matA);
RationalNumber[][] rank = MatrixOperations.matrixRank(matA); // row-reduced form
```

For any other numeric wrapper type (`Integer`, `Double`, etc.), the generic overloads apply automatically:

```java
Integer[][] matC = { {1, 2}, {3, 4} };
Integer[][] matD = { {5, 6}, {7, 8} };

Double[][] result = MatrixOperations.matrixMultiply(matC, matD);
Double det = MatrixOperations.matrixDet(matC);
```

## Design & Architecture

`matrixMultiply` and `matrixDet` work the same way logically for any numeric type — add things up, multiply things, know what zero is. `NumericOperations<T>` (`add`, `sub`, `mul`, `div`, `zero()`, `isZero()`) captures exactly that logic once, generically, instead of hardcoding arithmetic separately per type. Combined with `Function`/`BiFunction` for the handful of type-specific bits that aren't pure arithmetic (converting a value, allocating a result array), this is enough to implement each matrix operation once for *any* supported type, rather than once per type. `DoubleOperations` and `RationalNumberOperations` are the two concrete implementations (each a stateless singleton), and `RationalNumberOperations` uses a `RationalNumberFactory` to produce its zero value, since `RationalNumber` has no public zero-argument constructor of its own. Callers never deal with any of it — `matrixMultiply`/`matrixDet` simply take matrices in and return matrices out.

`AbstractRationalNumber` centralizes the arithmetic every `RationalNumber` implementation shares, working purely on raw numerator/denominator pairs (`absAdd`, `absSubtract`, `absMultiply`, `absDivide`) so `FractionRationalNumber` and `MixedRationalNumber` inherit correctness instead of re-deriving it, cancelling shared factors via `GCD` before multiplying to keep intermediate values smaller.

## Project Structure

| File | Description |
|---|---|
| `RationalNumber.java` | Core interface: arithmetic, `isZero`, accessors, `copy`, static `GCD` |
| `AbstractRationalNumber.java` | Shared numerator/denominator arithmetic (`absAdd`, `absSubtract`, `absMultiply`, `absDivide`) |
| `FractionRationalNumber.java` | Reduced-fraction implementation of `RationalNumber` |
| `MixedRationalNumber.java` | Mixed-number implementation of `RationalNumber` |
| `RationalNumberFactory.java` | Factory interface for producing a `RationalNumber` zero value |
| `FractionRationalNumberFactory.java` | Factory producing a `FractionRationalNumber` zero |
| `NumericOperations.java` | Strategy interface: `add`/`sub`/`mul`/`div`, `zero()`, `isZero()` for a type `T` |
| `DoubleOperations.java` | `NumericOperations<Double>` singleton, backing the `T extends Number` overloads |
| `RationalNumberOperations.java` | `NumericOperations<RationalNumber>` singleton, wraps a `RationalNumberFactory` |
| `MatrixOperations.java` | Static utility class: multiplication, determinant, rank, row operations, helpers |
