package services.entity

import java.util.UUID

import com.google.inject.{ImplementedBy, Inject}
import models.entity.Units
import models.exception.Err
import org.scalactic.{One, Or}
import services.db.repository.UnitsRepository

import scala.concurrent.Future

/**
  * Created by DT2 on 2019-06-21.
  */
@ImplementedBy(classOf[UnitsServiceImpl])
trait UnitsService {

  def read : Future[Seq[Units]]

  def write(units: Units) : Future[Units Or One[Err]]

  def edit(units: Units) : Future[Unit Or One[Err]]

  def delete(id : UUID) : Future[Unit Or One[Err]]

}

class UnitsServiceImpl @Inject()(val unitsRepo: UnitsRepository) extends UnitsService {

  override def read: Future[Seq[Units]] = unitsRepo.getUnits

  override def write(units: Units): Future[Or[Units, One[Err]]] = unitsRepo.addUnits(units)

  override def edit(units: Units): Future[Or[Unit, One[Err]]] = unitsRepo.updateUnits(units)

  override def delete(id: UUID): Future[Or[Unit, One[Err]]] = unitsRepo.deleteUnit(id)
}
