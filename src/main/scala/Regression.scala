import scala.math.{pow}

object Regression {
  def linear(xArray: Iterable[Double], yArray: Iterable[Double]) = {
    val n = xArray.size
    val nY = yArray.size

    require(n == nY, "Collections must have the same length!")

    val pairs = xArray zip yArray
    val sumX = pairs.par.foldLeft(0.0)((s, p) => s + p._1)
    val sumY = pairs.par.foldLeft(0.0)((s, p) => s + p._2)
    val sumX2 = pairs.par.foldLeft(0.0)((s, p) => s + pow(p._1, 2))
    val sumY2 = pairs.par.foldLeft(0.0)((s, p) => s + pow(p._2, 2))
    val sumXY = pairs.par.foldLeft(0.0)((s, p) => s + p._1 * p._2)

    val dn = n * sumX2 - pow(sumX, 2)
    if (dn == 0.0) throw new Exception("Can't solve the system!")

    val slope = ((n * sumXY) - (sumX * sumY)) / dn
    val intercept = ((sumY * sumX2) - (sumX * sumXY)) / dn

    val t1 = ((n * sumXY) - (sumX * sumY)) * ((n * sumXY) - (sumX * sumY))
    val t2 = (n * sumX2) - pow(sumX, 2)
    val t3 = (n * sumY2) - pow(sumY, 2)

    var r2 = 0.0
    if ((t2 * t3) != 0.0) r2 = t1 / (t2 * t3)

    (slope, intercept, r2)
  }
}
