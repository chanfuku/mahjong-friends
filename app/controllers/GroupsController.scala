package controllers

import models.{ Group, Player }
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scalikejdbc.DB

object GroupsController extends Controller {

  //グループ登録用Case FixMe:外出ししたい
  case class GroupWithUsers(groupName: String, playerNames: List[String])

  //グループ登録用Form FixMe:外出ししたい
  val groupRegistForm = Form(
    mapping(
      "groupName" -> nonEmptyText,
      "playerNames" -> list(nonEmptyText))(GroupWithUsers.apply)(GroupWithUsers.unapply))

  def lists() = Action { request =>
    DB autoCommit { implicit session =>
      val groups = Group.findAll()
      Ok(views.html.groups.list(groups))
    }
  }

  def input() = Action { request =>
    Ok(views.html.groups.input("グループ登録"))
  }

  def create() = Action { implicit request =>
    DB autoCommit { implicit session =>
      groupRegistForm.bindFromRequest.fold(
        formWithErrors => BadRequest("未入力の項目があります"),
        GroupWithUsers => {
          //グループを作成し、グループIDを取得する
          val groupId = Group.create(GroupWithUsers.groupName)
          //取得したグループIDで４人分のユーザを登録する
          GroupWithUsers.playerNames.foreach(Player.create(_, groupId))
          SeeOther(s"/groups/${groupId}")
        })
    }
  }

  def show(id: Long) = Action { implicit request =>
    DB autoCommit { implicit session =>
      //グループ情報(メンバー名、収支..etc)を取得する
      val players: List[Player] = Player.findByGroupId(id)
      players.headOption match {
        case Some(x) => Ok(views.html.groups.info(players, x.groupId))
        case None    => BadRequest
      }
    }
  }
}
