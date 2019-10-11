package services.db.table

import java.util.UUID

import models.entity.BillProducts
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
 * Created by DT2 on 2019-06-17.
 */


trait BillProductsTables {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class BillProductTable(tag: Tag) extends Table[BillProducts](tag, "BILL_PRODUCTS") {
    def * = (billId, prdId, unitId, quantity, price, isActive) <>
      ((BillProducts.apply _).tupled, BillProducts.unapply)

    def billId = column[UUID]("BILL_ID")

    def prdId = column[UUID]("PRD_ID")

    def unitId = column[UUID]("UNIT_ID")

    def quantity = column[Int]("QUANTITY")

    def price = column[Double]("PRICE")

    def isActive = column[Int]("IS_ACTIVE")

    def pk = primaryKey("PIPELINE_DD_VIEW_PK", (billId, prdId, unitId))

  }

}
