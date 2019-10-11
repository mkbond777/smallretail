package services.entity

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.entity.{Bills, Orders}
import models.exception.Err
import org.joda.time.DateTime
import org.scalactic.Accumulation._
import org.scalactic.{Bad, Good, One, Or, _}
import services.db.repository.BillsRepository

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[BillServiceImpl])
trait BillService {

  def read(custId : UUID) : Future[Seq[Bills]]

  def write(orders : Orders) : Future[Unit Or Every[Err]]

  def edit(bills: Bills) : Future[Unit Or One[Err]]

  def delete(id : UUID) : Future[Unit Or One[Err]]

}

class BillServiceImpl @Inject()(val billsRepo: BillsRepository,
                                val billProductsSer: BillProductsService)(implicit ec: ExecutionContext)
  extends BillService {

  override def read(custId : UUID) :  Future[Seq[Bills]] = billsRepo.getBills(custId)

  override def write(orders : Orders): Future[Or[Unit, Every[Err]]] = {
    billsRepo.addBill(Bills(orders.billId,orders.custId,DateTime.now(),DateTime.now(),
      orders.totalAmount,orders.paidAmount,1)).flatMap{
      case Good(bill) => Future.traverse(orders.products)(billProductsSer.insertOrUpdate).map(_.combined).flatMap{
        case Good(_) =>Future.successful(Good(Unit))
        case Bad(errs) => billsRepo.deleteBill(bill.id).map{
          case Good(_) => Bad(errs)
          case Bad(One(err)) => Bad(Every(err))
        }
      }
      case Bad(err) => Future.successful(Bad(err))
    }
  }

  override def edit(bills: Bills): Future[Or[Unit, One[Err]]] = billsRepo.updateBill(bills)

  override def delete(id: UUID): Future[Or[Unit, One[Err]]] = billProductsSer.bulkDelete(id).flatMap{
    case Good(_) => billsRepo.deleteBill(id)
    case Bad(err) => Future.successful(Bad(err))
  }
}
