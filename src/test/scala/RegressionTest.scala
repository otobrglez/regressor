import org.scalatest._

import scala.collection.mutable.ArrayBuffer

class RegressionTest extends FlatSpec with Matchers {
  it should "cry if collections have different size" in {
    a[IllegalArgumentException] should be thrownBy {
      Regression.linear(ArrayBuffer(1, 2), ArrayBuffer(2))
    }
  }

  it should "do simple regression" in {
    val x = ArrayBuffer(1, 2, 4, 5, 10, 20).map(_.toDouble)
    val y = ArrayBuffer(4, 6, 12, 15, 34, 68).map(_.toDouble)

    val results = Regression.linear(x, y)
    assert(results._1.formatted("%2.2f") == "3.44")
    assert(results._2.formatted("%2.2f") == "-0.89")
    assert(results._3.formatted("%2.3f") == "0.998")
  }
}
