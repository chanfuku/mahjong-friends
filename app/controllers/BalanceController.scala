package controllers

import models.{ Player, PaymentHistory, Event, BalanceHistory }
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

  def input(groupId: Long) = Action { request =>
    DB autoCommit { implicit session =>
      //グループ情報(メンバー名、収支..etc)を取得する
      val players: List[Player] = Player.findByGroupId(groupId)
      players.headOption match {
        case Some(x) => Ok(views.html.balance.input("収支登録", players, x.groupId))
        case None    => BadRequest
      }
    }
  }

  //  def history(groupId: Long) = Action { request =>
  //
  //  }

  def create() = Action { implicit request =>
    DB autoCommit { implicit session =>
      balanceRegistForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        f => {
          for {
            (playerId, i) <- f.playerIds.zipWithIndex
          } yield {
            val eventId = Event.create(f.date)
            val amount = f.amount(i)
            Player.findByPlayerId(playerId) match {
              case Some(x) => {
                PaymentHistory.create(playerId, f.groupId, eventId, amount)
                BalanceHistory.create(playerId, f.groupId, x.balance)
                Player.updateByGroupIdAndPlayerId(playerId, f.groupId, amount)
              }
              case None => NotFound
            }
          }
          SeeOther(s"/groups/${f.groupId}")
        })
    }
  }
}
