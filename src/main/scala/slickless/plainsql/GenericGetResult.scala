package slickless.plainsql

import slick.jdbc.GetResult

/**
  * Created on 18/12/2016.
  */
final case class GenericGetResult[T](value: GetResult[T])

object GenericGetResult {
  def apply[A](implicit g: GenericGetResult[A]) = g.value
}
