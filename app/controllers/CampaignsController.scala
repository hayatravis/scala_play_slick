package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.db.slick._
import slick.driver.JdbcProfile
import javax.inject.Inject
import scala.concurrent.Future
import slick.driver.H2Driver.api._
//import models.Campaign
//import dao.CampaignsDAO
import models.Tables._

import java.sql.Date


/**
 * Created by hayato_sg on 15/10/19.
 */
class CampaignsController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
									val messagesApi: MessagesApi) extends Controller
									with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

	def list = Action.async { implicit rs =>
		db.run(Campaigns.sortBy(t => t.id).result).map { campaigns =>
				println(campaigns)
			Ok(views.html.campaign.list(campaigns))
		}
	}

	import CampaignsController._
	def edit(id: Option[Long]) = Action.async { implicit rs =>
		// リクエストパラメータにIDが存在するか確認
		if(id.isDefined) {
			db.run(Campaigns.filter(t => t.id === id.get.bind).result.head).map { campaign =>
				// 値をフォームに詰める
				campaignForm.fill(CampaignForm(Some(campaign.id), campaign.name, campaign.title, campaign.contents_text, campaign.destination_url,
					Some(campaign.created), Some(campaign.modified), Some(campaign.deleted), Some(campaign.deleted_date)))
			}
		} else {
			Future { campaignForm }
		}

		Future{ Ok(views.html.campaign.edit(campaignForm)) }
		// .asyncを取って以下にするのもあり。
		// TODO .asyncをよく読む https://www.playframework.com/documentation/2.4.3/ScalaAsync (https://www.playframework.com/documentation/ja/2.3.x/ScalaAsync)
		// Ok(view.html.campaign.edit(form))

	}

	def create = Action.async { implicit rs =>
		// リクエスト内容をバインド
		campaignForm.bindFromRequest.fold(
			// エラーの場合
			error => {
				Future { BadRequest(views.html.campaign.edit(error)) }
			},
			// OKの場合
			form => {
				// キャンペーンを登録
				val campaign = CampaignsRow(0, form.name, form.title, form.contents_text, form.destination_url, form.created.get, form.modified.get, form.deleted.get, form.deleted_date.get)
				db.run(Campaigns += campaign).map { _ =>
					Redirect(routes.CampaignsController.list)
				}
			}
		)
	}
}

object CampaignsController {
	// フォームの値を格納するケースクラス
	case class CampaignForm(id: Option[Long], name: String, title: String, contents_text: String, destination_url: String,
			                       created: Option[String], modified: Option[String], deleted: Option[Int], deleted_date: Option[String])

	val campaignForm = Form(
		mapping(
			"id"                -> optional(longNumber),
			"name"              -> nonEmptyText(maxLength = 30),
			"title"             -> nonEmptyText(maxLength = 30),
			"contents_text"     -> nonEmptyText(maxLength = 100),
			"destination_url"   -> nonEmptyText(maxLength = 100),
			"created"           -> optional(text),
			"modified"          -> optional(text),
			"deleted"           -> optional(number(min = 0, max = 1)),
			"deleted_date"      -> optional(text)
		)(CampaignForm.apply)(CampaignForm.unapply)
	)
}