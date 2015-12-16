import scala.concurrent.duration.Duration
import scala.math.{pow}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Regression {
  def linear(xArray: Iterable[Double], yArray: Iterable[Double]) = {
    val n = xArray.size
    val nY = yArray.size

    require(n == nY, "Collections must have the same length!")

    val pairs = xArray zip yArray

    val sums = for {
      sumXi <- Future {
        pairs.foldLeft(0.0)((s, p) => s + p._1)
      }
      sumYi <- Future {
        pairs.foldLeft(0.0)((s, p) => s + p._2)
      }
      sumX2i <- Future {
        pairs.foldLeft(0.0)((s, p) => s + pow(p._1, 2))
      }
      sumY2i <- Future {
        pairs.foldLeft(0.0)((s, p) => s + pow(p._2, 2))
      }
      sumXYi <- Future {
        pairs.foldLeft(0.0)((s, p) => s + p._1 * p._2)
      }

    } yield (sumXi, sumYi, sumX2i, sumY2i, sumXYi)

    val (sumX, sumY, sumX2, sumY2, sumXY) = Await.result(sums, Duration.Inf)

    val dn = n * sumX2 - pow(sumX, 2)
    assert(dn != 0.0, "Can't solve the system!")

    val poms = for {
      slopei <- Future {
        ((n * sumXY) - (sumX * sumY)) / dn
      }
      intercepti <- Future {
        ((sumY * sumX2) - (sumX * sumXY)) / dn
      }
      t1i <- Future {
        ((n * sumXY) - (sumX * sumY)) * ((n * sumXY) - (sumX * sumY))
      }
      t2i <- Future {
        (n * sumX2) - pow(sumX, 2)
      }
      t31 <- Future {
        (n * sumY2) - pow(sumY, 2)
      }

    } yield (slopei, intercepti, t1i, t2i, t31)

    val (slope, intercept, t1, t2, t3) = Await.result(poms, Duration.Inf)

    if (t2 * t3 != 0.0)
      (slope, intercept, t1 / (t2 * t3))
    else
      (slope, intercept, 0.0)
  }
}
