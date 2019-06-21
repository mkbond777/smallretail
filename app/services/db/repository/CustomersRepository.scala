package services.db.repository

import com.google.inject.Inject
import javax.inject.Singleton
import models.entity.Customers
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.CustomerTables
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

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

}
