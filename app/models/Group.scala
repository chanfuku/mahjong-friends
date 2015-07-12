package models

import skinny.orm.SkinnyCRUDMapper
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import scalikejdbc.DBSession
import scalikejdbc.sqls

case class Group(id: Long, groupName: String)

object Group extends SkinnyCRUDMapper[Group] {
  override lazy val tableName = "groups"
  override lazy val defaultAlias = createAlias("g")

  def extract(rs: WrappedResultSet, rn: ResultName[Group]): Group = new Group(
    id = rs.get(rn.id),
    groupName = rs.string(rn.groupName))

  def findByGroupId(groupId: Long)(implicit session: DBSession): Option[Group] = {
    findBy(sqls.eq(defaultAlias.id, groupId))
  }

  def create(groupName: String)(implicit session: DBSession): Long = {
    createWithNamedValues(column.groupName -> groupName)
  }
}
