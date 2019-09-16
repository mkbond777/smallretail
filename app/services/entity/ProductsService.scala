package services.entity

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.entity.Products
import models.exception.Err
import org.scalactic.{One, Or}
import services.db.repository.ProductsRepository

import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[ProductsServiceImpl])
trait ProductsService {

  def read : Future[Seq[Products]]

  def write(products: Products) : Future[Products Or One[Err]]

  def edit(products: Products) : Future[Unit Or One[Err]]

  def delete(id : UUID) : Future[Unit Or One[Err]]

}

class ProductsServiceImpl @Inject()(val productsRepo: ProductsRepository) extends ProductsService {

  override def read: Future[Seq[Products]] = productsRepo.getProducts

  override def write(products: Products): Future[Or[Products, One[Err]]] = productsRepo.addProducts(products)

  override def edit(products: Products): Future[Or[Unit, One[Err]]] = productsRepo.updateProducts(products)

  override def delete(id: UUID): Future[Or[Unit, One[Err]]] = productsRepo.deleteProduct(id)
}
