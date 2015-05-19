package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class Player(id: Long, playerName: String, groupId: String, balance: Long, group: Option[Group] = None)

object Player extends SkinnyCRUDMapper[Player] {
  override lazy val tableName = "players"
  override lazy val defaultAlias = createAlias("p")

  belongsTo[Group](Group, (p, g) => p.copy(group = g)).byDefault

  def extract(rs: WrappedResultSet, rn: ResultName[Player]): Player = new Player(
    id = rs.get(rn.id),
    playerName = rs.string(rn.playerName),
    groupId = rs.string(rn.groupId),
    balance = rs.int(rn.balance))

  def findByPlayerId(playerId: String)(implicit session: DBSession): Option[Player] = {
    findBy(sqls.eq(defaultAlias.id, playerId))
  }

  def findByGroupId(groupId: Long)(implicit session: DBSession): List[Player] = {
    findAllBy(sqls.eq(defaultAlias.groupId, groupId))
  }

  def create(playerName: String, groupId: Long)(implicit session: DBSession): Long = {
    createWithNamedValues(column.playerName -> playerName, column.groupId -> groupId)
  }
}
