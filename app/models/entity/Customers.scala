package models.entity

import java.util.UUID

import play.api.libs.json.{Format, JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class Customers(id : UUID,
                     firstName:String,
                     lastName:Option[String],
                     phoneNumber:String,
                     seal:String,
                     isActive:Int)


trait CustomersFormat {
  val customerReads: Reads[Customers] = (
    (JsPath \ "id").readWithDefault[UUID](java.util.UUID.randomUUID()) and
      (JsPath \ "first_name").read[String] and
      (JsPath \ "last_name").readNullable[String] and
      (JsPath \ "phone_number").read[String] and
      (JsPath \ "seal").read[String] and
      (JsPath \ "is_active").readWithDefault[Int](1)
    )(Customers.apply _)

  val customerWrites: Writes[Customers] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "first_name").write[String] and
      (JsPath \ "last_name").writeOptionWithNull[String] and
      (JsPath \ "phone_number").write[String] and
      (JsPath \ "seal").write[String] and
      (JsPath \ "is_active").write[Int]
    )(unlift(Customers.unapply))

  implicit val entityVersionFormat: Format[Customers] = Format(customerReads, customerWrites)
}