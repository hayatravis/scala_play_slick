package controllers

import java.sql.Timestamp
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import javax.inject.Inject
import scala.concurrent.Future
import models.CampaignsRow
import dao.{SendLogDAO, CampaignsDAO}
import play.api.i18n.{MessagesApi, I18nSupport}

/**
 * Created by hayato_sg on 2015/10/26.
 */
class ReportController extends Controller {
	def daily_report(id: Option[Long]) = Action.async { implicit rs =>
		if (id.isDefined) {
			SendLogDAO.getDailyReport(id.get).map { logs =>
				Ok(views.html.report.daily_report(logs))
			}
		} else {
			Future { Redirect(routes.CampaignsController.list()) }
		}
	}
}
