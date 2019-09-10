package models.exception

/**
  * Created by DT2 on 2019-06-21.
  */
trait Err {
  def message: Option[String] = None

  def cause: Option[Throwable] = None
}

/** An Err whose concrete type indicates what the error is (e.g. Entity not found) - there is no need for message or
  * cause, but there may be parameters */
trait TypedErr extends Err

/**
  * An Err that indicates that a Throwable was caught
  *
  * @param throwable      the Throwable that caused this error
  * @param contextMessage an optional message describing the context (not the detail) of this error
  */
case class ThrowableErr(throwable: Throwable, contextMessage: Option[String] = None) extends Err {
  override def cause: Option[Throwable] = Some(throwable)

  override def message: Option[String] = contextMessage
}


object Err {
  def apply(cause: Throwable): Err = ThrowableErr(cause)

  def apply(message: String, cause: Throwable): Err = ThrowableErr(cause, Some(message))

  def unapply(err: Err): Option[(Option[String], Option[Throwable])] = Some(err.message, err.cause)
}

case class CustomerNotCreated() extends TypedErr

case class CustomerNotFound() extends TypedErr

