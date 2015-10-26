package dao

import scala.concurrent.Future
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.driver.H2Driver.api._
import language.higherKinds
import play.api.Play.current
import java.sql.Timestamp

// private[package name]
// about Tag http://stackoverflow.com/questions/31849946/scala-slick-table-tag
private[dao] abstract class BaseTable[CaseClassType](tableTag: Tag, tableName: String) extends Table[CaseClassType](tableTag, tableName) {
	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
	def created = column[Timestamp]("CREATED")
	def modified = column[Timestamp]("MODIFIED")
	def deleted = column[Int]("DELETED")
	def deleted_date = column[Timestamp]("DELETED_DATE")
}

// about <: : 上限境界(>: は下限境界)
private[dao] abstract trait BaseDao[TableClassType <: BaseTable[CaseClassType], CaseClassType] extends HasDatabaseConfig[JdbcProfile] {
	protected val dbConfig =  DatabaseConfigProvider.get[JdbcProfile]("default")

	val tableQuery : TableQuery[TableClassType]

	def getAll: Future[Seq[CaseClassType]] = db.run(tableQuery.filter(_.deleted === 0).result)

	// return insert id : http://stackoverflow.com/questions/21894377/returning-autoinc-id-after-insert-in-slick-2-0
	def insert(insertRecord: CaseClassType): Future[Long] = db.run((tableQuery returning tableQuery.map(_.id)) += insertRecord)

	def update(updateRecord: CaseClassType, id: Long) = db.run(tableQuery.filter(_.id === id).update(updateRecord))

	def findById(id: Long): Future[Option[CaseClassType]] = db.run(tableQuery.filter(_.id === id).result.headOption)

	// column name を引数にしたい
	// def countByColumnId(column_name: String, id: Long) = db.run(tableQuery.filter(_.column_name === id).result.length)
}