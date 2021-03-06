package neuroflow.nets

import breeze.linalg.DenseVector
import neuroflow.core.Activator.Linear
import neuroflow.core.EarlyStoppingLogic.CanAverage
import neuroflow.core._
import neuroflow.core.FFN.WeightProvider.oneWeights
import neuroflow.core.Network.Vector
import neuroflow.nets.DefaultNetwork._
import org.specs2.Specification
import org.specs2.specification.core.SpecStructure
import shapeless._
import breeze.numerics._
import breeze.stats._

import scala.collection.Seq

/**
  * @author bogdanski
  * @since 22.06.16
  */
class RegularizationTest extends Specification {

  def is: SpecStructure =
    s2"""

    This spec will test the regularization techniques.

    It should:
      - Check the early stopping logic                           $earlyStopping

  """

  def earlyStopping = {

    import neuroflow.common.VectorTranslation._

    val (xs, ys) = (Vector(Vector(1.0), Vector(2.0), Vector(3.0)), Vector(Vector(3.2), Vector(5.8), Vector(9.2)))

    val net = Network(Input(1) :: Hidden(3, Linear) :: Output(1, Linear) :: HNil,
      Settings(regularization = Some(EarlyStopping(xs, ys, 0.8))))

    implicit object KBL extends CanAverage[DefaultNetwork] {
      def averagedError(xs: Seq[Vector], ys: Seq[Vector]): Double = {
        val errors = xs.map(net.evaluate).zip(ys).toVector.map {
          case (a, b) => mean(abs(a.dv - b.dv))
        }.dv
        mean(errors)
      }
    }

    net.evaluate(Vector(1.0)) must be equalTo Vector(3.0)
    net.evaluate(Vector(2.0)) must be equalTo Vector(6.0)
    net.evaluate(Vector(3.0)) must be equalTo Vector(9.0)

    net.shouldStopEarly must be equalTo false
    net.shouldStopEarly must be equalTo true

  }

}
