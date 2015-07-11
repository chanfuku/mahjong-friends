package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.{ WrappedResultSet, ResultName, DBSession, sqls }

case class PaymentHistory(id: Long, playerId: Long, groupId: Long, eventId: Long, amount: Int)

object PaymentHistory extends SkinnyCRUDMapper[PaymentHistory] {
  override lazy val tableName = "payment_history"
  override lazy val defaultAlias = createAlias("ph")

  def extract(rs: WrappedResultSet, rn: ResultName[PaymentHistory]): PaymentHistory = new PaymentHistory(
    id = rs.get(rn.id),
    playerId = rs.int(rn.playerId),
    groupId = rs.int(rn.groupId),
    eventId = rs.int(rn.eventId),
    amount = rs.int(rn.amount))

  def create(playerId: Long, groupId: Long, eventId: Long, amount: Int): Long = {
    createWithNamedValues(
      column.playerId -> playerId,
      column.groupId -> groupId,
      column.eventId -> eventId,
      column.amount -> amount)
  }
}
