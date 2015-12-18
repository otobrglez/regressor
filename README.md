# Regressor

Linear and other regressions implemented in Scala.

[![Build Status](https://travis-ci.org/otobrglez/regressor.svg?branch=master)](https://travis-ci.org/otobrglez/regressor)

## Usage

### Linear regression

```scala
val results = Regression.linear(
  IndexedSeq(
    Seq(1, 4),
    Seq(2, 6),
    Seq(4, 12),
    Seq(5, 15),
    Seq(10, 34),
    Seq(20, 68)
  )
)
  
println(results)

//_1 => slope
//_2 => intercept
//_2 => r^2

// Linear function:
// y = slope*x - intercept

```

See [tests](src/test/scala) for more examples.

## Author

- [Oto Brglez](https://github.com/otobrglez)