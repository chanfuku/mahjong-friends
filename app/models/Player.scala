package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class Player(
  id: Long,
  playerName: String,
  groupId: String)

object Player extends SkinnyCRUDMapper[Player] {
  override lazy val tableName = "players"
  override lazy val defaultAlias = createAlias("p")

  def extract(rs: WrappedResultSet, rn: ResultName[Player]): Player = new Player(
    id = rs.get(rn.id),
    playerName = rs.string(rn.playerName),
    groupId = rs.string(rn.groupId))

  def findByPlayerId(playerId: String)(implicit session: DBSession): Option[Player] = {
    findBy(sqls.eq(defaultAlias.id, playerId))
  }

  def create(playerName: String, groupId: Long)(implicit session: DBSession): Long = {
    createWithNamedValues(column.playerName -> playerName, column.groupId -> groupId)
  }
}
