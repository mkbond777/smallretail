package services.db.repository

import java.util.UUID

import com.google.inject.Inject
import javax.inject.Singleton
import models.entity.Customers
import models.exception.{CustomerNotCreated, Err}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.CustomerTables
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by DT2 on 2019-06-21.
  */
@Singleton
class CustomersRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] with CustomerTables {

  import profile.api._

  private val customers = TableQuery[CustomerTable]

  def getCustomers : Future[Seq[Customers]] = {
    db.run(customers.result)
  }

  def addCustomers(customer: Customers) : Future[UUID Or One[Err]] = {
    db.run((customers returning customers.map(_.id)) += customer)
      .map(id => if (id.equals(null))
        Bad(One(CustomerNotCreated())) else Good(id))
  }

}
