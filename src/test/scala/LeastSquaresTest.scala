import org.scalatest.{Matchers, FlatSpec}

class LeastSquaresTest extends FlatSpec with Matchers {

  it should "do simple regression" in {
    val numbers = List(1, 2, 3, 4).zip(List(6, 5, 7, 10))
      .map({ case (a, b) => Seq(a.toDouble, b.toDouble) }).toIndexedSeq

    val results = Regression.leastSquares(numbers)

    assert(results._1.formatted("%2.2f") == "1.40")
    assert(results._2.formatted("%2.2f") == "3.50")
    assert(results._3.formatted("%2.2f") == "0.65")
    assert(results._4.formatted("%2.2f") == "1.77")
    assert(results._5(3.6) == 8.54)
  }
}
