package slickless.plainsql

import shapeless.{::, Generic, HList, HNil}
import slick.jdbc.{GetResult, PositionedResult}

/**
  * Created on 18/12/2016.
  */
trait HListGetResultImplicits {
  implicit object hnilGetResult extends GetResult[HNil] {
    def apply(r: PositionedResult) = HNil
  }

  implicit def hlistConsGetResult[H, T <: HList] (implicit h: GetResult[H], t: GetResult[T] ) =
    new GetResult[H :: T] {
      def apply(r: PositionedResult) = (r << h) :: t(r)
    }

  implicit val hnilGenericGetResult = GenericGetResult(new GetResult[HNil] {
    def apply(r: PositionedResult) = HNil
  })

  implicit def hlistConsGenericGetResult[H, T <: HList](implicit h: GetResult[H], t: GenericGetResult[T]): GenericGetResult[H :: T] = GenericGetResult(new GetResult[H :: T] {
    def apply(r: PositionedResult) = (r << h) :: t.value(r)
  })

  implicit def mkResult[A <: Product, AR <: HList](implicit gen: Generic.Aux[A, AR], g: GenericGetResult[AR]): GenericGetResult[A] =
    GenericGetResult(new GetResult[A] {
      def apply(r: PositionedResult): A = gen.from(g.value(r))
    })
}
