package models

import org.scalatest.FunSpec
import db.{ TestDBSetup, TestData }
import play.api.test.{ FakeApplication, FakeRequest }
import scalikejdbc.AutoSession
import play.api.test.Helpers
import play.api.test.Helpers._
import scalikejdbc._

class BalanceHistorySpec extends FunSpec with TestDBSetup with TestData {

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

  describe("BalanceHistory create()") {
    it("成功する") {
      withTestData {
        val id = BalanceHistory.create(playerId, groupId, balance)
        assert(BalanceHistory.findById(id) == Some(BalanceHistory(
          id,
          playerId,
          groupId,
          balance,
          Some(Player(playerId, playerName, groupId, balance, None)))))
      }
    }
  }
}
