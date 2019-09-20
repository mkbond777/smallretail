package models.entity

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads, Writes}

case class Units(id: UUID,
                 name: String,
                 description: Option[String],
                 isActive: Int)


trait UnitsFormat {
  protected val unitsReads: Reads[Units] = (
    (JsPath \ "id").readWithDefault[UUID](java.util.UUID.randomUUID()) and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "is_active").readWithDefault[Int](1)
    ) (Units.apply _)

  protected val unitsWrites: Writes[Units] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").writeOptionWithNull[String] and
      (JsPath \ "is_active").write[Int]
    ) (unlift(Units.unapply))

  protected implicit val unitsFormat: Format[Units] = Format(unitsReads, unitsWrites)
}