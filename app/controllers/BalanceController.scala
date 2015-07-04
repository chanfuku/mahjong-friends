package controllers

import models.{ Player, Balance, Event }
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scalikejdbc.DB
import java.util.Date

object BalanceController extends Controller {

  //収支登録用Case FixMe:外出ししたい
  case class BalanceForInsertData(playerIds: List[Long], groupId: Long, date: Date, amount: List[Int])

  //収支登録用Form FixMe:外だししたい
  val balanceRegistForm = Form(
    mapping(
      "playerIds" -> list(longNumber),
      "groupId" -> longNumber,
      "date" -> date("yyyy-MM-dd"),
      "amount" -> list(number))(BalanceForInsertData.apply)(BalanceForInsertData.unapply))

  def input(id: Long) = Action { request =>
    DB autoCommit { implicit session =>
      //グループ情報(メンバー名、収支..etc)を取得する
      val players: List[Player] = Player.findByGroupId(id)
      players.headOption match {
        case Some(x) => Ok(views.html.balance.input("収支登録", players, x.groupId))
        case None    => BadRequest
      }
    }
  }

  def create() = Action { implicit request =>
    DB autoCommit { implicit session =>
      balanceRegistForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        f => {
          val eventId = Event.create(f.date)
          f.playerIds.zipWithIndex.foreach {
            case (playerId, amount) =>
              Balance.create(playerId, f.groupId, eventId, amount)
              Player.updateByGroupIdAndPlayerId(playerId, f.groupId, amount)
          }
          SeeOther(s"/groups/${f.groupId}")
        })
    }
  }
}
