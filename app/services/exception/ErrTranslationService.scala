package services.exception

import com.google.inject.{ImplementedBy, Inject}
import models.exception.{CustomerNotCreated, CustomerNotFound, Err}
import play.api.i18n.{Lang, MessagesApi}

/**
  * Created by DT2 on 2019-06-23.
  */
@ImplementedBy(classOf[ErrTranslationServiceImpl])
trait ErrTranslationService {

  def translate(err: Err): String

}

class ErrTranslationServiceImpl @Inject()(val messageApi: MessagesApi) extends ErrTranslationService {

  implicit val lang: Lang = Lang(java.util.Locale.getDefault)

  override def translate(err: Err): String = err match {
    case CustomerNotCreated() => messageApi("customer.not.created")
    case CustomerNotFound() => messageApi("customer.not.found")
  }
}
