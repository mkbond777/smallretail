package services.db.table

import java.util.UUID

import models.entity.Products
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Created by DT2 on 2019-06-17.
  */


trait ProductsTables{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class ProductTable(tag : Tag) extends Table[Products](tag,"PRODUCTS"){
    def * = (id, name, pType, description,isActive) <>
      ((Products.apply _).tupled, Products.unapply)

    def id = column[UUID]("ID",O.PrimaryKey)

    def name = column[String]("NAME")

    def pType = column[String]("P_TYPE")

    def description = column[Option[String]]("DESCRIPTION")

    def isActive = column[Int]("IS_ACTIVE")
  }
}
