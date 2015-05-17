package controllers

import models.{ Group, Player }
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scalikejdbc.DB

object GroupsController extends Controller {

  //グループ登録用Case FixMe:外出ししたい
  case class GroupWithUsers(
    groupName: String,
    playerNames: List[String])

  //グループ登録用Form FixMe:外出ししたい
  val groupRegistForm = Form(
    mapping(
      "groupName" -> text,
      "playerNames" -> list(text))(GroupWithUsers.apply)(GroupWithUsers.unapply))

  def lists() = Action { request =>
    DB autoCommit { implicit session =>
      val groups = Group.findByGroupId("1")
      Ok(groups.toString())
    }
  }

  def input() = Action { request =>
    Ok(views.html.groups.input("グループ登録"))
  }

  def create() = Action { implicit request =>
    DB autoCommit { implicit session =>
      //グループを作成し、グループIDを取得する
      val groupName = groupRegistForm.bindFromRequest.get.groupName
      val groupId = Group.create(groupName)
      //TODO:取得したグループIDで４人分のユーザを登録する
      val playerNames = groupRegistForm.bindFromRequest.get.playerNames
      val playerIds = playerNames.map(x => Player.create(x, groupId))
      Ok(s"groupId:${groupId}, playerId:${playerIds.toString()}")
    }
  }
}
