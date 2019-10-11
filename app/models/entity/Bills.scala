package models.entity

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
//import play.api.libs.json.

case class Bills(id : UUID,
                 custId:UUID,
                 dateCreated:DateTime,
                 dateModified:DateTime,
                 totalAmount:Double,
                 paidAmount:Double,
                 isActive:Int)


trait BillsFormat {

  implicit val dateWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd hh:mm a")

  implicit val dateReads: Reads[DateTime] = JodaReads.jodaDateReads("yyyy-MM-dd hh:mm a")

  val billReads: Reads[Bills] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "cust_id").read[UUID] and
      (JsPath \ "date_created").read[DateTime] and
      (JsPath \ "date_modified").read[DateTime] and
      (JsPath \ "total_amount").read[Double] and
      (JsPath \ "paid_amount").read[Double] and
      (JsPath \ "is_active").read[Int]
    )(Bills.apply _)

  val billWrites: Writes[Bills] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "cust_id").write[UUID] and
      (JsPath \ "date_created").write[DateTime] and
      (JsPath \ "date_modified").write[DateTime] and
      (JsPath \ "total_amount").write[Double] and
      (JsPath \ "paid_amount").write[Double] and
      (JsPath \ "is_active").write[Int]
    )(unlift(Bills.unapply))

  implicit val customerFormat: Format[Bills] = Format(billReads, billWrites)
}