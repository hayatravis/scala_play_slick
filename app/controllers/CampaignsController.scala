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

import play.api.libs.json._

/**
 * Created by hayato_sg on 15/10/19.
 */
class CampaignsController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
									val messagesApi: MessagesApi) extends Controller
									with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

	def list = Action.async { implicit rs =>
		db.run(Campaigns.sortBy(t => t.id).result).map { campaigns =>
			Ok(views.html.campaign.list(campaigns))
		}
	}

	import CampaignsController._
	def edit(id: Option[Long]) = Action.async { implicit rs =>
		// リクエストパラメータにIDが存在するか確認
		val form = if(id.isDefined) {
			db.run(Campaigns.filter(t => t.id === id.get.bind).result.head).map { campaign =>
				val nowDate = new Date(System.currentTimeMillis()).getTime
				// 値をフォームに詰める
				campaignForm.fill(CampaignForm(Some(campaign.id), campaign.name, campaign.title, campaign.contents_text, campaign.destination_url,
					Some(nowDate), Some(nowDate), Some(campaign.deleted), Some(nowDate)))
			}

		} else {
			Future { campaignForm }
		}

		form.flatMap { form =>
			Future{ Ok(views.html.campaign.edit(form)) }
		}
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
//				val formatNow =  "%tY/%<tm/%<td %<tH:%<tM:%<tS" format new Date(System.currentTimeMillis())
				val nowDate = new Date(System.currentTimeMillis())
				// キャンペーンを登録
				val campaign = CampaignsRow(0, form.name, form.title, form.contents_text, form.destination_url, nowDate, nowDate, 0, nowDate)
				db.run(Campaigns += campaign).map { _ =>
					Redirect(routes.CampaignsController.list)
				}
			}
		)
	}

	def update = Action.async { implicit rs =>
		campaignForm.bindFromRequest.fold(
			// エラー
			error => {
				Future { BadRequest(views.html.campaign.edit(error)) }
			},
			form => {
//				val formatNow =  "%tY/%<tm/%<td %<tH:%<tM:%<tS" format new Date(System.currentTimeMillis())
				val nowDate = new Date(System.currentTimeMillis())
				val campaign = CampaignsRow(form.id.get, form.name, form.title, form.contents_text, form.destination_url, nowDate, nowDate, 0, nowDate)
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
			                       created: Option[Long], modified: Option[Long], deleted: Option[Int], deleted_date: Option[Long])

	val campaignForm = Form(
		mapping(
			"id"                -> optional(longNumber),
			"name"              -> nonEmptyText(maxLength = 30),
			"title"             -> nonEmptyText(maxLength = 30),
			"contents_text"     -> nonEmptyText(maxLength = 100),
			"destination_url"   -> nonEmptyText(maxLength = 100),
			"created"           -> optional(longNumber),
			"modified"          -> optional(longNumber),
			"deleted"           -> optional(number(min = 0, max = 1)),
			"deleted_date"      -> optional(longNumber)
		)(CampaignForm.apply)(CampaignForm.unapply)
	)
}