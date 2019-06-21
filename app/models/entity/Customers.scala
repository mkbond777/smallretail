package models.entity

import java.util.UUID

import play.api.libs.json.{Format, JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class Customers(id : UUID,
                     firstName:String,
                     lastName:String,
                     phoneNumber:String)


trait CustomersFormat{
  val customerReads: Reads[Customers] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "first_name").read[String] and
      (JsPath \ "last_name").read[String] and
      (JsPath \ "phone_number").read[String]
    )(Customers.apply _)

  val customerWrites: Writes[Customers] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "first_name").write[String] and
      (JsPath \ "last_name").write[String] and
      (JsPath \ "phone_number").write[String]
    )(unlift(Customers.unapply))

  implicit val entityVersionFormat: Format[Customers] = Format(customerReads, customerWrites)
}