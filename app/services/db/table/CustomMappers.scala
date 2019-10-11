package services.db.table

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Contains a set of custom Mapper which are not available with Slick
  */
trait CustomMappers {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    d => new Timestamp(d.getMillis),
    t => new DateTime(t.getTime)
  )

//  val dateMapper = MappedColumnType.base[DateTime, java.sql.Date](
//    d => new java.sql.Date(d.getMillis),
//    t => new DateTime(t.getTime)
//  )
//
//  implicit val seqMapper = MappedColumnType.base[Seq[String], String](
//    seq => seq.mkString(","),
//    str => str.split(",")
//  )

}
