package models

object Tables extends {
	val profile = slick.driver.H2Driver
} with Tables

trait Tables {
	val profile: slick.driver.JdbcProfile
	import profile.api._
	import java.sql.Date

	case class CampaignsRow(id: Long, name: String, title: String, contents_text: String, destination_url: String,
	                        created: Date, modified: Date, deleted: Int, deleted_date: Date)

	class Campaigns(_tableTag: Tag) extends Table[CampaignsRow](_tableTag, "CAMPAIGNS") {
		def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
		def name = column[String]("NAME")
		def title = column[String]("TITLE")
		def contents_text = column[String]("CONTENTS_TEXT")
		def destination_url = column[String]("DESTINATION_URL")
		def created = column[Date]("CREATED")
		def modified = column[Date]("MODIFIED")
		def deleted = column[Int]("DELETED")
		def deleted_date = column[Date]("DELETED_DATE")

		def * = (id, name, title, contents_text, destination_url, created, modified, deleted, deleted_date) <> (CampaignsRow.tupled, CampaignsRow.unapply)
	}

	lazy val Campaigns = new TableQuery(tag => new Campaigns(tag))
}