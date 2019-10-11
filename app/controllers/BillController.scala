package controllers

import java.util.UUID

import com.google.inject.Inject
import models.entity.{Bills, BillsFormat, Orders, OrdersFormat}
import models.exception.Err
import org.scalactic.{Bad, Good, Many, One}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.BillService
import services.exception.ErrTranslationService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-17.
  */
class BillController @Inject()(val configuration: Configuration,
                               val billSer : BillService,
                               val playBodyParsers: PlayBodyParsers,
                               val errTranslationService: ErrTranslationService)(implicit ec: ExecutionContext)
  extends InjectedController with OrdersFormat with BillsFormat {

  def bills(custId : UUID): Action[AnyContent] = Action.async{
    implicit request => billSer.read(custId).map(bills => Ok(Json.toJson(bills)))
  }

  def bill: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Orders] match {
      case JsSuccess(value, _) => billSer.write(value).map {
        case Good(_) => NoContent
        case Bad(One(err)) => val errMsg = errTranslationService.translate(err)
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
        case Bad(Many(firstErr, _, _@_*)) =>
          val errMsg = errTranslationService.translate(firstErr)
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
      }
      case e : JsError => Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))
    }
  }

  def editBill: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Bills] match {
      case JsSuccess(value, _) => billSer.edit(value).map{
        case Good(_) => NoContent
        case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
      }
      case e : JsError =>Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))
    }
  }

  def deleteBill(id : UUID): Action[AnyContent] = Action.async{
    implicit request => billSer.delete(id).map{
      case Good(_) => NoContent
      case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
    }
  }

  private def errMsg(err : One[Err]) : String = errTranslationService.translate(err.loneElement.asInstanceOf[Err])

}
