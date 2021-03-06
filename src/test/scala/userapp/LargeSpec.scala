package userapp

/* Ensure slickless compiles for large case classes with a nice set of imports. */

import shapeless._
import slick.jdbc.JdbcProfile
import slickless._


case class Large(
  a: Int, b: Int, c: Int, d: Int,
  e: Int, f: Int, g: Int, h: Int,
  i: Int, j: Int, k: Int, l: Int,
  m: Int, n: Int, o: Int, p: Int,
  q: Int, r: Int, s: Int, t: Int,
  u: Int, v: Int, w: Int, x: Int,
  y: Int, z: Int
)

object Types {
  type _26HList =
    Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: Int :: HNil

}

class TableWrapper(val driver: JdbcProfile) {
  import Types._
  import driver.api._
  class LargeTable(tag: Tag) extends Table[_26HList](tag, "large") {
    def a = column[Int]("a")

    def b = column[Int]("b")

    def c = column[Int]("c")

    def d = column[Int]("d")

    def e = column[Int]("e")

    def f = column[Int]("f")

    def g = column[Int]("g")

    def h = column[Int]("h")

    def i = column[Int]("i")

    def j = column[Int]("j")

    def k = column[Int]("k")

    def l = column[Int]("l")

    def m = column[Int]("m")

    def n = column[Int]("n")

    def o = column[Int]("o")

    def p = column[Int]("p")

    def q = column[Int]("q")

    def r = column[Int]("r")

    def s = column[Int]("s")

    def t = column[Int]("t")

    def u = column[Int]("u")

    def v = column[Int]("v")

    def w = column[Int]("w")

    def x = column[Int]("x")

    def y = column[Int]("y")

    def z = column[Int]("z")

    def * =
      a :: b :: c :: d ::
        e :: f :: g :: h ::
        i :: j :: k :: l ::
        m :: n :: o :: p ::
        q :: r :: s :: t ::
        u :: v :: w :: x ::
        y :: z :: HNil
      //.mappedWith(Generic[Large])
  }

  val LargeTable = TableQuery[LargeTable]
}

import org.scalatest.{Tag => _, _}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FreeSpec, Matchers}
import scala.concurrent.ExecutionContext.Implicits.global

class LargeSpec extends FreeSpec with Matchers with ScalaFutures {
  implicit val patience = PatienceConfig(timeout = Span(1, Seconds), interval = Span(250, Millis))
  import Types._
  "slick tables with >22 column mappings" - {
    "should support inserts and selects" in {
      val tableWrapper = new TableWrapper(slick.jdbc.H2Profile)
      import tableWrapper.driver.api._
      val LargeTable:TableQuery[tableWrapper.LargeTable] = tableWrapper.LargeTable
      //TableQuery[LargeTable]

      val db = Database.forConfig("h2")

      lazy val all = sql"""SELECT * FROM "large"""".as[_26HList]

      val hlist = Generic[Large]
      val large = hlist.to(Large(
         1,  2,  3,  4, 
         5,  6,  7,  8, 
         9, 10, 11, 12, 
        13, 14, 15, 16, 
        17, 18, 19, 20, 
        21, 22, 23, 24, 
        25, 26
      ))

      val action = for {
        _   <- LargeTable.schema.create
        _   <- LargeTable += large
        ans <- all.head
        //LargeTable.result.head
        _   <- LargeTable.schema.drop
      } yield {
        print(">>>> ")
        println(ans)
        ans
      }

      //db.run(action).foreach(println)

      whenReady(db.run(action)) { _ should equal(large) }
    }
  }
}
