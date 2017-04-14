package neuroflow.application.plugin

import java.util.concurrent.ThreadLocalRandom

/**
  * @author bogdanski
  * @since 20.01.16
  */
object Style {

  def ->[A](elems: A*): Vector[A] = elems.toVector
  def -->[A](elems: A*): Vector[A] = elems.toVector

  def infinity(dimension: Int): Vector[Double] = (0 until dimension).map(_ => Double.PositiveInfinity).toVector
  def ∞(dimension: Int): Vector[Double] = infinity(dimension)

  def random(dimension: Int): Vector[Double] = random(dimension, 0.0, 1.0)
  def random(dimension: Int, a: Double, b: Double): Vector[Double] =
    (0 until dimension).map(_ => ThreadLocalRandom.current.nextDouble(a, b)).toVector

  def partition(step: Int, n: Int): Set[Int] = Range.Int.inclusive(step - 1, step * n, step).toSet
  def Π(step: Int, n: Int): Set[Int] = partition(step, n)

}
