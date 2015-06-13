package controllers

import models.{ Player, Balance }
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scalikejdbc.DB

object BalanceController extends Controller {

  //収支登録用Case FixMe:外出ししたい
  case class BalanceForInsertData(playerIds: List[Long], groupId: Long, amount: List[Int])

  //収支登録用Form FixMe:外だししたい
  val balanceRegistForm = Form(
    mapping(
      "playerIds" -> list(longNumber),
      "groupId" -> longNumber,
      "amount" -> list(number))(BalanceForInsertData.apply)(BalanceForInsertData.unapply))

  def input(id: Long) = Action { request =>
    DB autoCommit { implicit session =>
      //グループ情報(メンバー名、収支..etc)を取得する
      val players: List[Player] = Player.findByGroupId(id)
      val groupId: Long = players(0).groupId
      Ok(views.html.balance.input("収支登録", players, groupId))
    }
  }

  def create() = Action { implicit request =>
    DB autoCommit { implicit session =>
      balanceRegistForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        f => f.playerIds.zipWithIndex.foreach { x =>
          val playerId = x._1
          val amount = f.amount(x._2)
          Balance.create(playerId, f.groupId, amount)
          Player.updateByGroupIdAndPlayerId(playerId, f.groupId, amount)
        })
    }
    Ok
  }
}
