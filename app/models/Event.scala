package models

import java.util.Date
import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class Event(id: Long, date: Date)

object Event extends SkinnyCRUDMapper[Event] {
  override lazy val tableName = "events"
  override lazy val defaultAlias = createAlias("e")

  def extract(rs: WrappedResultSet, rn: ResultName[Event]): Event = new Event(
    id = rs.get(rn.id),
    date = rs.date(rn.date))

  def create(date: Date)(implicit session: DBSession): Long = {
    createWithNamedValues(column.date -> date)
  }
}
