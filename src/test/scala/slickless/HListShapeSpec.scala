package slickless

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FreeSpec, Matchers}
import shapeless.{::, HNil}
import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class HListShapeSpec extends FreeSpec with Matchers with ScalaFutures {
  implicit val patience = PatienceConfig(timeout = Span(1, Seconds), interval = Span(250, Millis))

  class Users(tag: Tag) extends Table[Long :: String :: HNil](tag, "users") {
    def id    = column[Long]( "id", O.PrimaryKey, O.AutoInc )
    def email = column[String]("email")

    def * = id :: email :: HNil
  }

  lazy val users = TableQuery[Users]

  val db = Database.forConfig("h2")

  "slick tables with hlist mappings" - {
    "should support inserts and selects" in {


      users.schema.createStatements.foreach(println)

      val action = for {
        _   <- users.schema.create
        _   <- users += 1L :: "dave@example.com" :: HNil
        ans <- users.result.head
        //_   <- users.schema.drop
      } yield ans

      whenReady(db.run(action)) { _ should equal (1L :: "dave@example.com" :: HNil) }
    }
  }

  "slick getResult" - {
    "should return " in {

      users.schema.createStatements.foreach(println)

      lazy val all = sql"""select "id", "email" from "users"""".as[Long :: String :: HNil]

      users.result.statements.foreach(println)

      val action = for {
        _    <- users.schema.create
        _    <- users += 1L :: "dave@example.com" :: HNil
        user <- all.head
        _   <- users.schema.drop
      } yield {
        user
      }

      whenReady(db.run(action)) { _ should equal (1L :: "dave@example.com" :: HNil) }
    }
  }
}
