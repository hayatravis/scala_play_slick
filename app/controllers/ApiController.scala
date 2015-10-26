package controllers

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits._

import models.CampaignsRow
import models.SendLogRow
import dao.CampaignsDAO
import dao.SendLogDAO
import java.sql.Timestamp


object ApiController {
	implicit val campaignsRowWrites = (
			(__ \ "id"                  ).write[Option[Long]]       and
			(__ \ "name"                ).write[String]             and
			(__ \ "title"               ).write[String]             and
			(__ \ "contents_text"       ).write[String]             and
			(__ \ "destination_url"     ).write[String]             and
			(__ \ "created"             ).write[Option[Timestamp]]       and
			(__ \ "modified"            ).write[Option[Timestamp]]       and
			(__ \ "deleted"             ).write[Option[Int]]        and
			(__ \ "deleted_date"        ).write[Option[Timestamp]]
		)(unlift(CampaignsRow.unapply))
}

/**
 * Created by hayato_sg on 15/10/21.
 */
class ApiController extends Controller {

	import ApiController._
	def campaign(id: Option[Long]) = Action.async { implicit rs =>
		if (id.isDefined) {
			// ログの挿入
//			val nowDate = new Date(System.currentTimeMillis())
			val nowTimestamp = new Timestamp(System.currentTimeMillis())
			val Log = SendLogRow(Some(0), id.get, rs.remoteAddress, rs.headers.get("User-Agent").get, Some(nowTimestamp), Some(0))
			println(SendLogDAO.insert(Log))

			CampaignsDAO.findById(id.get).map { campaign =>
				Ok(Json.obj("campaigns" -> campaign))
			}
		} else {
			CampaignsDAO.getAll.map { campaigns =>
				Ok(Json.obj("campaigns" -> campaigns))
			}
		}
	}
}
