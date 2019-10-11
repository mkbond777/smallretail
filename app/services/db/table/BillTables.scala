package services.db.table

import java.util.UUID

import models.entity.Bills
import org.joda.time.DateTime
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
 * Created by DT2 on 2019-06-17.
 */


trait BillTables extends CustomMappers {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class BillTable(tag: Tag) extends Table[Bills](tag, "BILLS") {
    def * = (id, custId, dateCreated, dateModified, totalAmount, paidAmount,isActive) <>
      ((Bills.apply _).tupled, Bills.unapply)

    def id = column[UUID]("ID", O.PrimaryKey)

    def custId = column[UUID]("CUST_ID")

    def dateCreated = column[DateTime]("DATE_CREATED")

    def dateModified = column[DateTime]("DATE_MODIFIED")

    def totalAmount = column[Double]("TOTAL_AMOUNT")

    def paidAmount = column[Double]("PAID_AMOUNT")

    def isActive = column[Int]("IS_ACTIVE")

  }

}
