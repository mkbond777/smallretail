package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

case class Orders(billId : UUID,
                  custId:UUID,
                  totalAmount:Double,
                  paidAmount:Double,
                  products: Seq[BillProducts])


trait OrdersFormat extends BillProductsFormat {
  val ordersReads: Reads[Orders] = (
    (JsPath \ "bill_id").read[UUID] and
      (JsPath \ "cust_id").read[UUID] and
      (JsPath \ "total_amount").read[Double] and
      (JsPath \ "paid_amount").read[Double] and
      (JsPath \ "products").read[Seq[BillProducts]]
    )(Orders.apply _)

  val ordersWrites: Writes[Orders] = (
    (JsPath \ "bill_id").write[UUID] and
      (JsPath \ "cust_id").write[UUID] and
      (JsPath \ "total_amount").write[Double] and
      (JsPath \ "paid_amount").write[Double] and
      (JsPath \ "products").write[Seq[BillProducts]]
    )(unlift(Orders.unapply))

  implicit val orderFormat: Format[Orders] = Format(ordersReads, ordersWrites)
}