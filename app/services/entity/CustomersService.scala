package services.entity

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.entity.Customers
import models.exception.Err
import org.scalactic.{One, Or}
import services.db.repository.CustomersRepository

import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[CustomersServiceImpl])
trait CustomersService {

  def read : Future[Seq[Customers]]

  def write(customers: Customers) : Future[Customers Or One[Err]]

}

class CustomersServiceImpl @Inject()(val customersRepo: CustomersRepository) extends CustomersService {

  override def read: Future[Seq[Customers]] = customersRepo.getCustomers

  override def write(customers: Customers): Future[Or[Customers, One[Err]]] = customersRepo.addCustomers(customers)
}
