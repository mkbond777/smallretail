package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class Orders(billId : UUID,
                  custId:UUID,
                  totalAmount:Double,
                  paidAmount:Double,
                  products: Seq[BillProducts])


trait OrdersFormat extends BillProductsFormat {
  implicit val ordersReads: Reads[Orders] = (
    (JsPath \ "bill_id").read[UUID] and
      (JsPath \ "cust_id").read[UUID] and
      (JsPath \ "total_amount").read[Double] and
      (JsPath \ "paid_amount").read[Double] and
      (JsPath \ "products").read[Seq[BillProducts]]
    )(Orders.apply _)
}