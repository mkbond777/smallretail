package services.db.table

import java.util.UUID

import models.entity.Units
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Created by DT2 on 2019-06-17.
  */


trait UnitsTables{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class UnitTable(tag : Tag) extends Table[Units](tag,"UNITS"){
    def * = (id, name, description,isActive) <>
      ((Units.apply _).tupled, Units.unapply)

    def id = column[UUID]("ID",O.PrimaryKey)

    def name = column[String]("NAME")

    def description = column[Option[String]]("DESCRIPTION")

    def isActive = column[Int]("IS_ACTIVE")
  }
}
