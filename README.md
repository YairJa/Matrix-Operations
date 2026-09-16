# Matrix Operations 

A small Java library for performing matrix operations — multiplication, row reduction / rank, determinant, and elementary row operations — over any type that extends `Number`, with dedicated, exact support for rational numbers via a custom `RationalNumber` interface.

## Features

- **Matrix multiplication**
  - Generic version for any `T extends Number` (returns a `double[][]`)
  - Exact version for `RationalNumber[][]` (returns `RationalNumber[][]`, no floating-point rounding)
- **Determinant** (`matrixDet`)
  - Recursive cofactor expansion, with a generic `double`-based version and an exact `RationalNumber`-based version
  - Row/column selection is optimized to reduce the number of recursive calls (see `maxZeroRow` below)
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

double[][] result = MatrixOperations.matrixMultiply(matC, matD);
double det = MatrixOperations.matrixDet(matC);
```

## Design & Architecture

Most operations that involve accumulating a running total — `matrixMultiply` and `matrixDet` — exist in two forms: a generic version for any `T extends Number` that works in `double`, and an exact version for `RationalNumber` that never leaves rational arithmetic. Structural helpers that don't accumulate numbers, like `minorMatrix` and `rowInterchange`, don't need this split and are written once, fully generically, for reuse by both paths. `RationalNumber` exists because the intended use case — determinants, rank, row reduction — is exactly where floating-point rounding is most likely to give a wrong answer (e.g. a near-singular matrix reading as singular, or vice versa); staying exact matters more here than raw performance.

That exactness is centralized rather than duplicated: `AbstractRationalNumber` implements all four arithmetic operations once, generically, in terms of raw numerator/denominator pairs (`absAdd`, `absSubtract`, `absMultiply`, `absDivide`). `FractionRationalNumber` and `MixedRationalNumber` both inherit this rather than reimplementing arithmetic themselves — each only handles its own representation (reducing a fraction vs. splitting off a whole part). A specific representation could in principle compute some operations more directly, but correctness lives in one place instead of being re-derived per class.

Where possible, generic helpers avoid hard-coding what "zero" means: `findFirstNonZeroIdx` and `maxZeroRow` take a `Predicate<T> isZero`, so the same traversal logic works for `RationalNumber` (`num -> num.isZero()`) and any `Number` (`n -> n.doubleValue()==0`) without duplicating the scan itself. This isn't applied everywhere yet — the same approach could extend to more of the basic arithmetic helpers.

Determinant calculation also uses a small factory: `matrixDet` needs a "zero" to accumulate the cofactor sum into, but a generic `RationalNumber` has no public zero-argument constructor. `RationalNumberFactory` (implemented by `FractionRationalNumberFactory`) supplies that value via `factory.zero()`. The same idea would work for `matrixMultiply`, but it's avoided there on purpose — the accumulator is initialized from the first term of the sum instead (`LeftMat[i][0].multiply(RightMat[0][j])`), which sidesteps needing a zero element at the cost of a slightly less uniform loop.

## Project Structure

| File | Description |
|---|---|
| `RationalNumber.java` | Core interface: arithmetic, `isZero`, accessors, `copy`, static `GCD` |
| `AbstractRationalNumber.java` | Shared numerator/denominator arithmetic (`absAdd`, `absSubtract`, `absMultiply`, `absDivide`) |
| `FractionRationalNumber.java` | Reduced-fraction implementation of `RationalNumber` |
| `MixedRationalNumber.java` | Mixed-number implementation of `RationalNumber` |
| `RationalNumberFactory.java` | Factory interface for producing a `RationalNumber` zero value |
| `FractionRationalNumberFactory.java` | Factory producing a `FractionRationalNumber` zero |
| `MatrixOperations.java` | Static utility class: multiplication, determinant, rank, row operations, helpers |
