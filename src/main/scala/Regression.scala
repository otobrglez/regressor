import scala.concurrent.duration.Duration
import scala.math.{pow, sqrt}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Regression {
  def linear(pairs: IndexedSeq[Seq[Double]]) = {
    val n = pairs.size

    val fSumI = Future(pairs.map(x => x(0)).sum)
    val fSumXi = Future(pairs.map(x => x(1)).sum)
    val fSumX2i = Future(pairs.map(x => pow(x(0), 2)).sum)
    val fSumY2i = Future(pairs.map(x => pow(x(1), 2)).sum)
    val fSumXYi = Future(pairs.map(x => x(0) * x(1)).sum)

    val sums = for {
      sumXi <- fSumI
      sumYi <- fSumXi
      sumX2i <- fSumX2i
      sumY2i <- fSumY2i
      sumXYi <- fSumXYi
    } yield (sumXi, sumYi, sumX2i, sumY2i, sumXYi)

    val (sumX, sumY, sumX2, sumY2, sumXY) = Await.result(sums, Duration.Inf)

    val dn = n * sumX2 - pow(sumX, 2)
    assert(dn != 0.0, "Can't solve the system!")

    val fSlopei = Future {
      ((n * sumXY) - (sumX * sumY)) / dn
    }

    val fIntercepti = Future {
      ((sumY * sumX2) - (sumX * sumXY)) / dn
    }

    val fT1i = Future {
      ((n * sumXY) - (sumX * sumY)) * ((n * sumXY) - (sumX * sumY))
    }

    val fT2i = Future {
      (n * sumX2) - pow(sumX, 2)
    }

    val fT31 = Future {
      (n * sumY2) - pow(sumY, 2)
    }

    val poms = for {
      slopei <- fSlopei
      intercepti <- fIntercepti
      t1i <- fT1i
      t2i <- fT2i
      t31 <- fT31

    } yield (slopei, intercepti, t1i, t2i, t31)

    val (slope, intercept, t1, t2, t3) = Await.result(poms, Duration.Inf)

    if (t2 * t3 != 0.0)
      (slope, intercept, t1 / (t2 * t3))
    else
      (slope, intercept, 0.0)
  }

  def leastSquares(pairs: IndexedSeq[Seq[Double]]) = {
    val n = pairs.size

    val fSumXi = Future(pairs.map(x => x(0)).sum)
    val fSumYi = Future(pairs.map(x => x(1)).sum)
    val fSumXYi = Future(pairs.map(x => x(0) * x(1)).sum)
    val fSumXSqi = Future(pairs.map(x => pow(x(0), 2)).sum)

    val pro = for {
      sumXi <- fSumXi
      sumYi <- fSumYi
      sumXYi <- fSumXYi
      sumXSqi <- fSumXSqi
    } yield (sumXi, sumYi, sumXYi, sumXSqi)

    val (sumX, sumY, sumXY, sumXSq) = Await.result(pro, Duration.Inf)
    val m = (sumXY - sumX * sumY / n) / (sumXSq - sumX * sumX / n)
    val b = sumY / n - m * sumX / n

    // Errors
    var sum = 0.0
    for (pair <- pairs) {
      sum += (pair(1) - b - m * pair(0)) *
        (pair(1) - b - m * pair(0))
    }

    val delta = n * sumXSq - pow(sumX, 2)
    val vari = 1.0 / (n - 2.0) * sum
    val b_err = sqrt(vari / delta * sumXSq)

    val m_err = sqrt(n / delta * vari)

    (m, b, m_err, b_err, (x: Double) => {
      m * x + b
    })
  }
}
