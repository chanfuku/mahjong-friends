package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class Balance(id: Long, playerId: Long, groupId: Long, amount: Int)

object Balance extends SkinnyCRUDMapper[Balance] {
  override lazy val tableName = "balance_history"
  override lazy val defaultAlias = createAlias("b")

  def extract(rs: WrappedResultSet, rn: ResultName[Balance]): Balance = new Balance(
    id = rs.get(rn.id),
    playerId = rs.int(rn.playerId),
    groupId = rs.int(rn.groupId),
    amount = rs.int(rn.amount))

  def create(playerId: Long, groupId: Long, amount: Int): Long = {
    createWithNamedValues(
      column.playerId -> playerId,
      column.groupId -> groupId,
      column.amount -> amount)
  }

}
