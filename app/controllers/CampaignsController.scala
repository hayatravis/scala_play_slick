package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import javax.inject.Inject
import scala.concurrent.Future
import models.CampaignsRow
import dao.CampaignsDAO
import play.api.i18n.{MessagesApi, I18nSupport}


/**
 * Created by hayato_sg on 15/10/19.
 */
class CampaignsController @Inject() (val messagesApi: MessagesApi)  extends Controller with I18nSupport {

	def list = Action.async { implicit rs =>
		CampaignsDAO.getAll.map { campaigns =>
			Ok(views.html.campaign.list(campaigns))
		}
	}

	import CampaignsController._
	def edit(id: Option[Long]) = Action.async { implicit rs =>
			if (id.isDefined) {
				CampaignsDAO.findById(id.get).map { campaign =>
					campaign match {
						case Some(c) => campaignForm.fill(CampaignForm(c.id, c.name, c.title, c.contents_text, c.destination_url))
						case _ => campaignForm
					}
				}.map( r =>
					Ok(views.html.campaign.edit(r))
				)
			} else {
				Future{ Ok(views.html.campaign.edit(campaignForm)) }
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
				// キャンペーンを登録
				val campaignsRow = CampaignsRow(Some(0), form.name, form.title, form.contents_text, form.destination_url)
				CampaignsDAO.insert(campaignsRow).map { _ =>
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
				val campaign = CampaignsRow(form.id, form.name, form.title, form.contents_text, form.destination_url)
				CampaignsDAO.update(campaign, form.id.get).map { _ =>
					Redirect(routes.CampaignsController.list)
				}
			}
		)
	}
}

object CampaignsController {
	// フォームの値を格納するケースクラス
	case class CampaignForm(id: Option[Long], name: String, title: String, contents_text: String, destination_url: String)

	val campaignForm = Form(
		mapping(
			"id"                -> optional(longNumber),
			"name"              -> nonEmptyText(maxLength = 30),
			"title"             -> nonEmptyText(maxLength = 30),
			"contents_text"     -> nonEmptyText(maxLength = 100),
			"destination_url"   -> nonEmptyText(maxLength = 100)
		)(CampaignForm.apply)(CampaignForm.unapply)
	)
}