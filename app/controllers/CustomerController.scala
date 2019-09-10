package controllers

import java.util.UUID

import com.google.inject.Inject
import models.entity.{Customers, CustomersFormat}
import models.exception.Err
import org.scalactic.{Bad, Good, One}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.CustomersService
import services.exception.ErrTranslationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-17.
  */
class CustomerController @Inject()(val configuration: Configuration,
                                   val customersSer: CustomersService,
                                   val playBodyParsers: PlayBodyParsers,
                                   val errTranslationService: ErrTranslationService)
  extends InjectedController with CustomersFormat{

  def customers: Action[AnyContent] = Action.async{
    implicit request => customersSer.read.map(cust =>
      Ok(Json.toJson(cust)))
  }

  def customer: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Customers] match {
      case JsSuccess(value, _) => customersSer.write(value).map {
        case Good(customer) => Created(Json.toJson(customer))
        case Bad(err) =>
          val errMsg = errTranslationService.translate(err.loneElement.asInstanceOf[Err])
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
      }
      case e : JsError => Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))

    }
  }

  def editCustomer: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Customers] match {
      case JsSuccess(value, _) => customersSer.edit(value).map{
        case Good(_) => NoContent
        case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
      }
      case e : JsError =>Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))
    }
  }

  def deleteCustomer(id : UUID): Action[AnyContent] = Action.async{
    implicit request => customersSer.delete(id).map{
      case Good(_) => NoContent
      case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
    }
  }

  private def errMsg(err : One[Err]) : String = errTranslationService.translate(err.loneElement.asInstanceOf[Err])

}
