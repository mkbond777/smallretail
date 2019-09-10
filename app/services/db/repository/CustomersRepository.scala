package services.db.repository


import java.util.UUID

import com.google.inject.Inject
import javax.inject.Singleton
import models.entity.Customers
import models.exception.{CustomerNotCreated, CustomerNotFound, Err}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.CustomerTables
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@Singleton
class CustomersRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with CustomerTables {

  import profile.api._

  private val custTable = TableQuery[CustomerTable]

  def getCustomers : Future[Seq[Customers]] = {
    db.run(custTable.filter(_.isActive === 1).result)
  }

  def addCustomers(customer: Customers) : Future[Customers Or One[Err]] = {
    db.run((custTable returning custTable += customer)
      .map(cust => if (cust.equals(null))
        Bad(One(CustomerNotCreated())) else Good(cust)))
  }

  def updateCustomers(custs: Customers) : Future[Unit Or One[Err]] = {
    db.run(custTable.filter(c => c.id === custs.id && c.isActive === 1)
      .map(c => (c.firstName,c.lastName,c.phoneNumber,c.seal))
      .update(custs.firstName, custs.lastName, custs.phoneNumber, custs.seal)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(CustomerNotFound()))
    }
  }

  def deleteCustomer(id : UUID) : Future[Unit Or One[Err]] = {
    db.run(custTable.filter(c => c.id === id && c.isActive === 1)
      .map(_.isActive).update(0)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(CustomerNotFound()))
    }
  }

}
