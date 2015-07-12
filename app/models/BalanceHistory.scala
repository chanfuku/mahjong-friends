package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class BalanceHistory(id: Long, playerId: Long, groupId: Long, balance: Long, player: Option[Player] = None)

object BalanceHistory extends SkinnyCRUDMapper[BalanceHistory] {
  override lazy val tableName = "balance_history"
  override lazy val defaultAlias = createAlias("b_his")

  belongsTo[Player](Player, (b, p) => b.copy(player = p)).byDefault

  def extract(rs: WrappedResultSet, rn: ResultName[BalanceHistory]): BalanceHistory = new BalanceHistory(
    id = rs.get(rn.id),
    playerId = rs.int(rn.playerId),
    groupId = rs.int(rn.groupId),
    balance = rs.int(rn.balance))

  def create(playerId: Long, groupId: Long, balance: Long): Long = {
    createWithNamedValues(
      column.playerId -> playerId,
      column.groupId -> groupId,
      column.balance -> balance)
  }

  def findBalance(playerId: Long, groupId: Long): List[BalanceHistory] = {
    findAllBy(sqls.eq(defaultAlias.playerId, playerId).and.eq(defaultAlias.groupId, groupId))
  }
}
