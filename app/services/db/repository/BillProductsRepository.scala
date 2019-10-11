package services.db.repository

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.entity.BillProducts
import models.exception.{BillProductNotFound, BillProductNotUpdated, Err}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.BillProductsTables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by DT2 on 2019-06-21.
 */
@Singleton
class BillProductsRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                      (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with BillProductsTables {

  import profile.api._

  private val billProductTable = TableQuery[BillProductTable]

  def getProducts(id: UUID): Future[Seq[BillProducts]] = {
    db.run(billProductTable.filter(bp => bp.isActive === 1 && bp.billId === id).result)
  }

  def addBillProducts(billProducts: BillProducts): Future[Unit Or One[Err]] = {
    db.run(billProductTable.insertOrUpdate(billProducts)).map {
      case 1 => Good(Unit)
      case _ => Bad(One(BillProductNotUpdated()))
    }
  }

  def deleteBillProduct(billId: UUID, prdId: UUID, unitId: UUID): Future[Unit Or One[Err]] = {
    db.run(billProductTable.filter(bp => bp.isActive === 1 && bp.billId === billId &&
      bp.prdId === prdId && bp.unitId === unitId).map(_.isActive).update(0)
    ).map {
      case 1 => Good(Unit)
      case _ => Bad(One(BillProductNotFound()))
    }
  }

  def bulkDeleteBillPrd(billId: UUID): Future[Unit Or One[Err]] = {
    db.run(billProductTable.filter(bp => bp.isActive === 1 && bp.billId === billId)
      .map(_.isActive).update(0)).map {
      case 1 => Good(Unit)
      case _ => Bad(One(BillProductNotFound()))
    }
  }

}
