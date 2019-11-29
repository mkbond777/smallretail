package services.db.repository

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.entity.{BillProducts, BillProductsWrites}
import models.exception.{BillProductNotFound, BillProductNotUpdated, Err}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.{BillProductsTables, ProductsTables, UnitsTables}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by DT2 on 2019-06-21.
 */
@Singleton
class BillProductsRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                      (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile]
    with BillProductsTables
    with ProductsTables
    with UnitsTables {

  import profile.api._

  private val billProductTable = TableQuery[BillProductTable]

  private val productTable = TableQuery[ProductTable]

  private val unitTable = TableQuery[UnitTable]

  def getProducts(id: UUID): Future[Seq[BillProductsWrites]] = {
    val activeBillProducts = billProductTable.filter(bp => bp.isActive === 1 && bp.billId === id)
    val activeProducts = productTable.filter(_.isActive === 1)
    val activeUnits = unitTable.filter(_.isActive === 1)

    val query = for {
      ((billPrd, prd), units) <- activeBillProducts join activeProducts on (_.prdId === _.id ) join activeUnits on (_
        ._1.unitId === _.id)
    } yield (billPrd.billId, prd, units, billPrd.quantity, billPrd.price, billPrd.isActive)

    db.run(query.result).map(items => items.map((BillProductsWrites.apply _).tupled(_)))
  }

  def addBillProducts(billProducts: BillProducts): Future[Unit Or One[Err]] =

    db.run(billProductTable.insertOrUpdate(billProducts)).map {
      case 1 => Good(Unit)
      case _ => Bad(One(BillProductNotUpdated()))
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
