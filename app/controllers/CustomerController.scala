package controllers

import com.google.inject.Inject
import models.entity.CustomersFormat
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, InjectedController}
import services.entity.CustomersService

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by DT2 on 2019-06-17.
  */
class CustomerController @Inject()(val configuration: Configuration,
                                   val customersSer: CustomersService)
  extends InjectedController with CustomersFormat{

  def customers: Action[AnyContent] = Action.async{
    implicit request => customersSer.read.map(cust => Ok(Json.toJson(cust)))
  }

}
