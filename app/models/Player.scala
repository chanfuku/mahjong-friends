package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc._

case class Player(id: Long, playerName: String, groupId: Long, balance: Long, group: Option[Group] = None)

object Player extends SkinnyCRUDMapper[Player] {
  override lazy val tableName = "players"
  lazy val tableNameForSQLSyntax = sqls"players"
  override lazy val defaultAlias = createAlias("p")

  belongsTo[Group](Group, (p, g) => p.copy(group = g)).byDefault

  def extract(rs: WrappedResultSet, rn: ResultName[Player]): Player = new Player(
    id = rs.get(rn.id),
    playerName = rs.string(rn.playerName),
    groupId = rs.int(rn.groupId),
    balance = rs.int(rn.balance))

  def findByPlayerId(playerId: Long)(implicit session: DBSession): Option[Player] = {
    findBy(sqls.eq(defaultAlias.id, playerId))
  }

  def findByGroupId(groupId: Long)(implicit session: DBSession): List[Player] = {
    findAllBy(sqls.eq(defaultAlias.groupId, groupId))
  }

  def create(playerName: String, groupId: Long)(implicit session: DBSession): Long = {
    createWithNamedValues(column.playerName -> playerName, column.groupId -> groupId)
  }

  def updateByGroupIdAndPlayerId(playerId: Long, groupId: Long, amount: Int)(implicit session: DBSession) = {
    val query = sql"""UPDATE ${tableNameForSQLSyntax} SET balance = balance + ${amount} 
                  WHERE id = ${playerId} AND group_id = ${groupId};"""
    query.update().apply()
  }
}
