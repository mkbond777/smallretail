package services.entity

import com.google.inject.{ImplementedBy, Inject}
import models.entity.Customers
import services.db.repository.CustomersRepository

import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[CustomersServiceImpl])
trait CustomersService {

  def read : Future[Seq[Customers]]

}

class CustomersServiceImpl @Inject()(val customersRepo: CustomersRepository) extends CustomersService {
  override def read: Future[Seq[Customers]] = customersRepo.getCustomers
}
