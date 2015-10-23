package dao

import slick.driver.H2Driver.api._

import models.CampaignsRow

class CampaignsTable(tag: Tag) extends BaseTable[CampaignsRow] (tag, "CAMPAIGNS") {
	def name = column[String]("NAME")
	def title = column[String]("TITLE")
	def contents_text = column[String]("CONTENTS_TEXT")
	def destination_url = column[String]("DESTINATION_URL")

	def * = (id.?, name, title, contents_text, destination_url, created.?, modified.?, deleted.?, deleted_date.?) <> (CampaignsRow.tupled, CampaignsRow.unapply)
}

object CampaignsDAO extends BaseDao[CampaignsTable, CampaignsRow] {
	val tableQuery = TableQuery[CampaignsTable]
}