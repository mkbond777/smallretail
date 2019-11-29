package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}

case class BillProducts(billId: UUID,
                        prdId: UUID,
                        unitId: UUID,
                        quantity: Int,
                        price: Double,
                        is_active: Int)

case class BillProductsWrites(billId : UUID,
                              products : Products,
                              unit: Units,
                              quantity: Int,
                              price: Double,
                              is_active: Int)


trait BillProductsFormat extends ProductsFormat with UnitsFormat {
  implicit val billProductsReads: Reads[BillProducts] = (
    (JsPath \ "bill_id").read[UUID] and
      (JsPath \ "prd_id").read[UUID] and
      (JsPath \ "unit_id").read[UUID] and
      (JsPath \ "quantity").read[Int] and
      (JsPath \ "price").read[Double] and
      (JsPath \ "is_active").readWithDefault[Int](1)
    ) (BillProducts.apply _)

  implicit val billProductsWrites: Writes[BillProductsWrites] = (
    (JsPath \ "bill_id").write[UUID] and
      (JsPath \ "product").write[Products] and
      (JsPath \ "unit").write[Units] and
      (JsPath \ "quantity").write[Int] and
      (JsPath \ "price").write[Double] and
      (JsPath \ "is_active").write[Int]
    ) (unlift(BillProductsWrites.unapply))

}