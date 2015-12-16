# Regressor

Linear and other regressions implemented in Scala.

[![Build Status](https://travis-ci.org/otobrglez/regressor.svg?branch=master)](https://travis-ci.org/otobrglez/regressor)

## Usage

```scala
val results = Regression.linear(
    ArrayBuffer(1, 2, 4, 5, 10, 20),
    ArrayBuffer(4, 6, 12, 15, 34, 68)
)  
  
println(results)

//_1 => slope
//_2 => intercept
//_2 => r^2

// Linear function:
// y = slope*x - intercept

```

## Author

- [Oto Brglez](https://github.com/otobrglez)