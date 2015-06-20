package db

import scalikejdbc.DBSession
import scalikejdbc._

trait TestData {

  //有効データ
  lazy val groupId = 1
  lazy val groupName = "亀北沢ボーイズ"
  lazy val playerId = 1
  lazy val playerName = "イーペー太郎"
  lazy val eventId = 1

  //存在しないデータ
  lazy val unExGroupId = 999
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
