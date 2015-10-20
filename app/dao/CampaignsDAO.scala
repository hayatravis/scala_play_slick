//package dao
//
//import scala.concurrent.Future
//import java.sql.Date
//
//import language.higherKinds
//
//import play.api.Play
//import play.api.db.slick.DatabaseConfigProvider
//import play.api.db.slick.HasDatabaseConfig
//import slick.driver.JdbcProfile
//import slick.driver.MySQLDriver.api._
//
//import models.Campaign
//
//class CampaignsTable(tag: Tag) extends Table[Campaign] (tag, "CAMPAIGNS") {
//	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
//	def name = column[String]("NAME")
//	def title = column[String]("TITLE")
//	def contents_text = column[String]("CONTENTS_TEXT")
//	def destination_url = column[String]("DESTINATION_URL")
//	def created = column[String]("CREATED")
//	def modified = column[String]("MODIFIED")
//	def deleted = column[Int]("DELETED")
//	def deleted_date = column[String]("DELETED_DATE")
//
//	def * = (id.?, name, title, contents_text, destination_url, created, modified, deleted, deleted_date) <> (Campaign.tupled, Campaign.unapply)
//}
//
//object CampaignsDAO {
//	lazy val Campaigns = new TableQuery(tag => new CampaignsTable(tag))
//}
//
