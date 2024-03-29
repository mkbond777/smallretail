package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

case class Products(id: UUID,
                    name: String,
                    pType: String,
                    description: Option[String],
                    isActive: Int)


trait ProductsFormat {
  val productReads: Reads[Products] = (
    (JsPath \ "id").readWithDefault[UUID](java.util.UUID.randomUUID()) and
      (JsPath \ "name").read[String] and
      (JsPath \ "p_type").read[String] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "is_active").readWithDefault[Int](1)
    ) (Products.apply _)

  val productWrites: Writes[Products] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "name").write[String] and
      (JsPath \ "p_type").write[String] and
      (JsPath \ "description").writeOptionWithNull[String] and
      (JsPath \ "is_active").write[Int]
    ) (unlift(Products.unapply))

  implicit val productFormat: Format[Products] = Format(productReads, productWrites)
}