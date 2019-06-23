package models

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsObject, Json, Writes}


/**
  * Created by DT2 on 2019-06-22.
  */
case class Response(isSuccess : Boolean,
                    message: String,
                    result: JsValueWrapper)

object ResponseHelper {
   implicit val responseWrites = new Writes[Response] {
     def writes(response: Response): JsObject = Json.obj(
       "IS_SUCCESS" -> response.isSuccess,
       "MESSAGE" -> response.message,
       "RESULT" -> response.result
     )
   }
}