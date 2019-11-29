package services.entity

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.entity.{BillProducts, BillProductsWrites}
import models.exception.Err
import org.scalactic.{One, Or}
import services.db.repository.BillProductsRepository

import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[BillProductsServiceImpl])
trait BillProductsService {

  def read(billId : UUID) : Future[Seq[BillProductsWrites]]

  def insertOrUpdate(billProducts: BillProducts) : Future[Unit Or One[Err]]

  def delete(billId : UUID, prdId: UUID, unitId : UUID) : Future[Unit Or One[Err]]

  def bulkDelete(billId : UUID) : Future[Unit Or One[Err]]

}

class BillProductsServiceImpl @Inject()(val billProductsRepo: BillProductsRepository) extends BillProductsService {

  override def read(billId : UUID) : Future[Seq[BillProductsWrites]] = billProductsRepo.getProducts(billId)

  override def insertOrUpdate(billProducts: BillProducts) : Future[Unit Or One[Err]] =
    billProductsRepo.addBillProducts(billProducts)

  override def delete(billId : UUID, prdId: UUID, unitId : UUID) : Future[Unit Or One[Err]] =
    billProductsRepo.deleteBillProduct(billId,prdId,unitId)

  override def bulkDelete(billId : UUID) : Future[Unit Or One[Err]] = billProductsRepo.bulkDeleteBillPrd(billId)
}
