package db

import scalikejdbc.DBSession
import scalikejdbc._

trait TestData {

  val groupId = 1
  val groupName = "亀北沢ボーイズ"
  val playerId = 1
  val playerName = "イーペー太郎"
  val eventId = 1

  def createGroup()(implicit session: DBSession) {
    sql"INSERT INTO groups (id, group_name) VALUES (${groupId},${groupName})".update().apply()
  }
  def createPlayer(playerId: Int, playerName: String)(implicit session: DBSession) {
    val balance = playerId * 1000
    sql"""INSERT INTO players (id, player_name, group_id, balance)
          VALUES(${playerId}, ${playerName}, ${groupId}, ${balance})
          """.update().apply()
  }

  def createEvent()(implicit session: DBSession) {
    sql"INSERT INTO events (id, date) VALUES (${eventId}, now())".update().apply()
  }
}
