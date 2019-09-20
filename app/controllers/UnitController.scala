package controllers

import java.util.UUID

import com.google.inject.Inject
import models.entity.{Units, UnitsFormat}
import models.exception.Err
import org.scalactic.{Bad, Good, One}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.UnitsService
import services.exception.ErrTranslationService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-17.
  */
class UnitController @Inject()(val configuration: Configuration,
                               val unitsService: UnitsService,
                               val playBodyParsers: PlayBodyParsers,
                               val errTranslationService: ErrTranslationService)(implicit ec: ExecutionContext)
  extends InjectedController with UnitsFormat {

  def units: Action[AnyContent] = Action.async{
    implicit request => unitsService.read.map(units => Ok(Json.toJson(units)))
  }

  def unit: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Units] match {
      case JsSuccess(value, _) => unitsService.write(value).map {
        case Good(unit) => Created(Json.toJson(unit))
        case Bad(err) =>
          val errMsg = errTranslationService.translate(err.loneElement.asInstanceOf[Err])
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
      }
      case e : JsError => Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))

    }
  }

  def editUnit: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Units] match {
      case JsSuccess(value, _) => unitsService.edit(value).map{
        case Good(_) => NoContent
        case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
      }
      case e : JsError =>Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))
    }
  }

  def deleteUnit(id : UUID): Action[AnyContent] = Action.async{
    implicit request => unitsService.delete(id).map{
      case Good(_) => NoContent
      case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
    }
  }

  private def errMsg(err : One[Err]) : String = errTranslationService.translate(err.loneElement.asInstanceOf[Err])

}
