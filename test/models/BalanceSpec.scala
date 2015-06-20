package models

import org.scalatest.FunSpec
import db.{ TestDBSetup, TestData }
import play.api.test.{ FakeApplication, FakeRequest }
import scalikejdbc.AutoSession
import play.api.test.Helpers
import play.api.test.Helpers._
import scalikejdbc._

class BalanceSpec extends FunSpec with TestDBSetup with TestData {

  val amount = 1

  def withTestData(test: => Any) = {
    implicit val session = AutoSession
    try {
      deleteAll()
      createGroup()
      createPlayer(playerId, playerName)
      createEvent()
      test
    } finally {
      deleteAll()
    }
  }

  describe("Balance create()") {
    it("成功する") {
      withTestData {
        val id = Balance.create(playerId, groupId, eventId, amount)
        assert(Balance.findById(id) == Some(Balance(id, playerId, groupId, eventId, amount)))
      }
    }
  }
}
