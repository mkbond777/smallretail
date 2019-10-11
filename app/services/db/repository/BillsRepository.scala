package services.db.repository

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.entity.Bills
import models.exception.{BillNotCreated, BillNotFound, Err}
import org.joda.time.DateTime
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.BillTables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-21.
  */
@Singleton
class BillsRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with BillTables {

  import profile.api._

  private val billTable = TableQuery[BillTable]

  private def filterBill(id : UUID) =
    billTable.filter(b => b.id === id && b.isActive === 1)

  def getBills(custId : UUID) : Future[Seq[Bills]] = {
    db.run(billTable.filter(b =>b.isActive === 1 && b.custId === custId).result)
  }

  def addBill(bills: Bills) : Future[Bills Or One[Err]] = {
    db.run((billTable returning billTable += bills)
      .map(bill => if (bill.equals(null))
        Bad(One(BillNotCreated())) else Good(bill)))
  }

  def updateBill(bills: Bills) : Future[Unit Or One[Err]] = {
    db.run(filterBill(bills.id).map(b => (b.dateModified,b.totalAmount,b.paidAmount))
      .update(DateTime.now(), bills.totalAmount,bills.paidAmount)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(BillNotFound()))
    }
  }

  def deleteBill(id : UUID) : Future[Unit Or One[Err]] = {
    db.run(filterBill(id).map(_.isActive).update(0)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(BillNotFound()))
    }
  }

}
