package controllers

import com.google.inject.Inject
import models.Response
import models.entity.{Customers, CustomersFormat}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.CustomersService
import models.ResponseHelper._
import models.exception.Err
import org.scalactic.{Bad, Good}
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
      Ok(Json.toJson(Response(isSuccess = true,message = "List of customers",cust))))
  }

  def customer: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Customers] match {

      case JsSuccess(value, _) => customersSer.write(value).map {
        case Good(id) => Ok(Json.toJson(Response(isSuccess = true,message = "Customer created",
          Json.obj("ID"->id))))
        case Bad(err) =>
          val errMsg = errTranslationService.translate(err.loneElement.asInstanceOf[Err])
          Ok(Json.toJson(Response(isSuccess = false, errMsg, Json.obj("ID"->""))))
      }
      case e : JsError => Future(Ok(Json.toJson(Response(isSuccess = false,message = "Unable to create customer",
        JsError.toJson(e)))))

    }
  }

}
