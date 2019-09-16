package controllers

import java.util.UUID

import com.google.inject.Inject
import models.entity.{Products, ProductsFormat}
import models.exception.Err
import org.scalactic.{Bad, Good, One}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.ProductsService
import services.exception.ErrTranslationService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-17.
  */
class ProductController @Inject()(val configuration: Configuration,
                                  val productsSer: ProductsService,
                                  val playBodyParsers: PlayBodyParsers,
                                  val errTranslationService: ErrTranslationService)(implicit ec: ExecutionContext)
  extends InjectedController with ProductsFormat {

  def products: Action[AnyContent] = Action.async{
    implicit request => productsSer.read.map(prd => Ok(Json.toJson(prd)))
  }

  def product: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Products] match {
      case JsSuccess(value, _) => productsSer.write(value).map {
        case Good(product) => Created(Json.toJson(product))
        case Bad(err) =>
          val errMsg = errTranslationService.translate(err.loneElement.asInstanceOf[Err])
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
      }
      case e : JsError => Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))

    }
  }

  def editProduct: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[Products] match {
      case JsSuccess(value, _) => productsSer.edit(value).map{
        case Good(_) => NoContent
        case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
      }
      case e : JsError =>Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))
    }
  }

  def deleteProduct(id : UUID): Action[AnyContent] = Action.async{
    implicit request => productsSer.delete(id).map{
      case Good(_) => NoContent
      case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
    }
  }

  private def errMsg(err : One[Err]) : String = errTranslationService.translate(err.loneElement.asInstanceOf[Err])

}
