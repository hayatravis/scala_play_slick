package controllers

import java.sql.Date
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import javax.inject.Inject
import scala.concurrent.Future
import slick.driver.H2Driver.api._

import play.api.libs.json._
import play.api.libs.functional.syntax._


object ApiController {
	implicit val campaignsRowWrites = (
			(__ \ "id"                  ).write[Long]       and
			(__ \ "name"                ).write[String]     and
			(__ \ "title"               ).write[String]     and
			(__ \ "contents_text"       ).write[String]     and
			(__ \ "destination_url"     ).write[String]     and
			(__ \ "created"             ).write[Date]     and
			(__ \ "modified"            ).write[Date]     and
			(__ \ "deleted"             ).write[Int]     and
			(__ \ "deleted_date"        ).write[Date]
		)(unlift(CampaignsRow.unapply))
}

/**
 * Created by hayato_sg on 15/10/21.
 */
class ApiController @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends Controller
								with HasDatabaseConfigProvider[JdbcProfile] {

	import ApiController._
	def campaign(id: Option[Long]) = Action.async { implicit rs =>
//		var isExist = Future{ false }
		if (id.isDefined) {
			// TODO idが存在しないパターンに対応する
//			db.run(Campaigns.filter(t => t.id === id.get.bind).exists.result).map { campaign =>
			db.run(Campaigns.filter(t => t.id === id.get.bind).result).map { campaign =>
				Ok(Json.obj("campaigns" -> campaign))
			}
		} else {
			db.run(Campaigns.sortBy(t => t.id).result).map { campaigns =>
				Ok(Json.obj("campaigns" -> campaigns))
			}
		}
	}
}
