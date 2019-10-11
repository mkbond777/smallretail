package controllers

import java.util.UUID

import com.google.inject.Inject
import models.entity.{BillProducts, BillProductsFormat}
import models.exception.Err
import org.scalactic.{Bad, Good, One}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, PlayBodyParsers}
import services.entity.BillProductsService
import services.exception.ErrTranslationService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-17.
  */
class BillProductController @Inject()(val configuration: Configuration,
                                      val billProductsSer: BillProductsService,
                                      val playBodyParsers: PlayBodyParsers,
                                      val errTranslationService: ErrTranslationService)(implicit ec: ExecutionContext)
  extends InjectedController with BillProductsFormat {

  def billProducts(billId : UUID): Action[AnyContent] = Action.async{
    implicit request => billProductsSer.read(billId).map(billProducts => Ok(Json.toJson(billProducts)))
  }

  def billProduct: Action[JsValue] = Action.async(playBodyParsers.json){
    implicit request => request.body.validate[BillProducts] match {
      case JsSuccess(value, _) => billProductsSer.insertOrUpdate(value).map {
        case Good(_) => NoContent
        case Bad(err) =>
          val errMsg = errTranslationService.translate(err.loneElement.asInstanceOf[Err])
          ServiceUnavailable(Json.toJson(Json.obj("message"->errMsg)))
      }
      case e : JsError => Future(BadRequest(Json.toJson(Json.obj("message" ->
        s"validation error: $e"))))

    }
  }

  def deleteBillProducts(billId : UUID, prdId: UUID, unitId : UUID): Action[AnyContent] = Action.async{
    implicit request => billProductsSer.delete(billId,prdId,unitId).map{
      case Good(_) => NoContent
      case Bad(err) => NotFound(Json.toJson(Json.obj("message"->errMsg(err))))
    }
  }

  private def errMsg(err : One[Err]) : String = errTranslationService.translate(err.loneElement.asInstanceOf[Err])

}
