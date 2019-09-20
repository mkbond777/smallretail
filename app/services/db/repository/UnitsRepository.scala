package services.db.repository

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.entity.Units
import models.exception.{CustomerNotFound, Err, UnitNotCreated, UnitNotFound}
import org.scalactic.{Bad, Good, One, Or}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.db.table.UnitsTables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by DT2 on 2019-06-21.
  */
@Singleton
class UnitsRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with UnitsTables {

  import profile.api._

  private val unitTable = TableQuery[UnitTable]

  private def filterUnits(id : UUID) =
    unitTable.filter(u => u.id === id && u.isActive === 1)

  def getUnits : Future[Seq[Units]] = {
    db.run(unitTable.filter(_.isActive === 1).result)
  }

  def addUnits(units: Units) : Future[Units Or One[Err]] = {
    db.run((unitTable returning unitTable += units)
      .map(unit => if (unit.equals(null))
        Bad(One(UnitNotCreated())) else Good(unit)))
  }

  def updateUnits(units: Units) : Future[Unit Or One[Err]] = {
    db.run(filterUnits(units.id).map(u => (u.name,u.description)).update(units.name, units.description)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(UnitNotFound()))
    }
  }

  def deleteUnit(id : UUID) : Future[Unit Or One[Err]] = {
    db.run(filterUnits(id).map(_.isActive).update(0)
    ).map{
      case 1 => Good(Unit)
      case _ => Bad(One(CustomerNotFound()))
    }
  }

}
