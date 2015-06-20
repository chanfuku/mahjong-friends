package controllers

import org.scalatest.FunSpec
import db.{ TestDBSetup, TestData }
import play.api.test.{ FakeApplication, FakeRequest }
import scalikejdbc.AutoSession
import play.api.test.Helpers
import play.api.test.Helpers._
import scalikejdbc._

class BalanceControllerSpec extends FunSpec with TestDBSetup with TestData {

  val baseURL = "/balance/input"

  def withTestData(test: => Any) = {
    implicit val session = AutoSession
    try {
      deleteAll()
      createGroup()
      (1 to 4).map(x => createPlayer(x, s"player${x}"))
      test
    } finally {
      deleteAll()
    }
  }

  describe("BalanceController input()") {
    it("OKを返す") {
      running(FakeApplication()) {
        withTestData {
          val result = route(FakeRequest(GET, s"${baseURL}/${groupId}")).get
          assert(status(result) == OK)
        }
      }
    }
  }
}
