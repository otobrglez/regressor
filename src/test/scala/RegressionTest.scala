import org.scalatest._
import scala.collection.mutable.ArrayBuffer

class RegressionTest extends FlatSpec with Matchers {

  it should "cry if system has no solution" in {
    a[AssertionError] should be thrownBy {
      Regression.linear(IndexedSeq(Seq(0, 0), Seq(0, 0)))
    }
  }

  it should "do simple regression" in {
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

    assert(results._1.formatted("%2.2f") == "3.44")
    assert(results._2.formatted("%2.2f") == "-0.89")
    assert(results._3.formatted("%2.3f") == "0.998")
  }

  it should "work from file" in {
    val fileName = "src/test/resources/data/numbers_simple.csv"
    val results = Regression.linear(io.Source.fromFile(fileName)
      .getLines
      .map(_.split(",")
        .map(_.toDouble).to[collection.immutable.Seq]
      ).toIndexedSeq)

    assert(results._1.formatted("%2.2f") == "3.44")
  }
}
