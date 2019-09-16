package services.db.repository

import java.util.UUID

import com.google.inject.Inject
import javax.inject.Singleton
import models.entity.Products
import models.exception.{CustomerNotFound, Err, ProductNotCreated, ProductNotFound}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.ProductsTables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-21.
  */
@Singleton
class ProductsRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with ProductsTables {

  import profile.api._

  private val productTable = TableQuery[ProductTable]

  def getProducts : Future[Seq[Products]] = {
    db.run(productTable.filter(_.isActive === 1).result)
  }

  def addProducts(products: Products) : Future[Products Or One[Err]] = {
    db.run((productTable returning productTable += products)
      .map(prd => if (prd.equals(null))
        Bad(One(ProductNotCreated())) else Good(prd)))
  }

  def updateProducts(prd: Products) : Future[Unit Or One[Err]] = {
    db.run(productTable.filter(c => c.id === prd.id && c.isActive === 1)
      .map(c => (c.name,c.pType,c.description)).update(prd.name, prd.pType, prd.description)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(ProductNotFound()))
    }
  }

  def deleteProduct(id : UUID) : Future[Unit Or One[Err]] = {
    db.run(productTable.filter(c => c.id === id && c.isActive === 1)
      .map(_.isActive).update(0)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(CustomerNotFound()))
    }
  }

}
