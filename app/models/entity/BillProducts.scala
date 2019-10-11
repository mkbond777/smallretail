package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

case class BillProducts(billId: UUID,
                        prdId: UUID,
                        unitId: UUID,
                        quantity: Int,
                        price: Double,
                        is_active: Int)


trait BillProductsFormat {
  protected val billProductsReads: Reads[BillProducts] = (
    (JsPath \ "bill_id").read[UUID] and
      (JsPath \ "prd_id").read[UUID] and
      (JsPath \ "unit_id").read[UUID] and
      (JsPath \ "quantity").read[Int] and
      (JsPath \ "price").read[Double] and
      (JsPath \ "is_active").readWithDefault[Int](1)
    ) (BillProducts.apply _)

  protected val billProductsWrites: Writes[BillProducts] = (
    (JsPath \ "bill_id").write[UUID] and
      (JsPath \ "prd_id").write[UUID] and
      (JsPath \ "unit_id").write[UUID] and
      (JsPath \ "quantity").write[Int] and
      (JsPath \ "price").write[Double] and
      (JsPath \ "is_active").write[Int]
    ) (unlift(BillProducts.unapply))

  protected implicit val billProductsFormat: Format[BillProducts] = Format(billProductsReads, billProductsWrites)
}