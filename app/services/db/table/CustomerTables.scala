package services.db.table

import java.util.UUID

import models.entity.Customers
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Created by DT2 on 2019-06-17.
  */


trait CustomerTables{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class CustomerTable(tag : Tag) extends Table[Customers](tag,"CUSTOMERS"){
    def * = (id, firstName, lastName, phoneNumber) <>
      ((Customers.apply _).tupled, Customers.unapply)

    def id = column[UUID]("ID",O.PrimaryKey)

    def firstName = column[String]("FIRST_NAME")

    def lastName = column[String]("LAST_NAME")

    def phoneNumber = column[String]("PHONE_NUMBER")
  }
}
