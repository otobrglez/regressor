import scala.concurrent.duration.Duration
import scala.math.{pow}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Regression {
  def linear(pairs: IndexedSeq[Seq[Double]]) = {
    val n = pairs.size

    val sums = for {
      sumXi <- Future {
        var sum = 0.0
        for (pair <- pairs) sum += pair(0)
        sum
      }
      sumYi <- Future {
        var sum = 0.0
        for (pair <- pairs) sum += pair(1)
        sum
      }
      sumX2i <- Future {
        var sum = 0.0
        for (pair <- pairs) sum += pow(pair(0), 2)
        sum
      }
      sumY2i <- Future {
        var sum = 0.0
        for (pair <- pairs) sum += pow(pair(1), 2)
        sum
      }
      sumXYi <- Future {
        var sum = 0.0
        for (pair <- pairs) sum += pair(0) * pair(1)
        sum
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

  def leastSquares(pairs: IndexedSeq[Seq[Double]]) = {
    val n = pairs.size

    val pro = for {
      sumXi <- Future {
        var sum = 0.0
        for (pair <- pairs) {
          sum += pair(0)
        }
        sum
      }
      sumYi <- Future {
        var sum = 0.0
        for (pair <- pairs) {
          sum += pair(1)
        }
        sum
      }
      sumXYi <- Future {
        var sum = 0.0
        for (pair <- pairs) {
          sum += pair(0) * pair(1)
        }
        sum
      }
      sumXSqi <- Future {
        var sum = 0.0
        for (pair <- pairs) {
          sum += pow(pair(0), 2)
        }
        sum
      }
    } yield (sumXi, sumYi, sumXYi, sumXSqi)

    val (sumX, sumY, sumXY, sumXSq) = Await.result(pro, Duration.Inf)
    val m = (sumXY - sumX * sumY / n) / (sumXSq - sumX * sumX / n)
    val b = sumY / n - m * sumX / n
    (m, b)
  }
}
